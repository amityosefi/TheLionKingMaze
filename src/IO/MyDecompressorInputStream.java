package IO;

import algorithms.mazeGenerators.Maze;

import java.io.*;

public class MyDecompressorInputStream extends InputStream {
    private InputStream in;
    private int _compIndex;
    private int _uncompIndex;
    private int _crntValue;

    public MyDecompressorInputStream(InputStream InputStream) {
        in = InputStream;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] byteArray) throws IOException
    {
        byte[] compressedData = new byte[byteArray.length];
        //read the input into the compressedData array, return the number of the actual bytes that read (the rest of the array contains 0)
        int numOfReadBytes = in.read(compressedData);
        _compIndex = 0;
        _uncompIndex = 0;
        //decompress the data
        decompressFunc(byteArray, compressedData, numOfReadBytes);

        return 0;
    }

    public void decompressFunc(byte[] byteArray, byte[] compressedData, int numOfReadBytes) {
        // Copy maze header
        for (int i = 0; i < Maze.mazeHeaderLen; i++)
            byteArray[i] = compressedData[i];

        _crntValue = compressedData[Maze.mazeHeaderLen]; //read value of first char
        _compIndex = Maze.mazeHeaderLen + 1; //because in the compressed array the mazeHeaderLen index is the first value
        _uncompIndex = Maze.mazeHeaderLen;

        while (_compIndex < numOfReadBytes) {
            switch (compressedData[_compIndex] & 0xF0)
            {
                case 0xF0:
                    decompressCodeF(compressedData, byteArray, numOfReadBytes);
                    break;
                case 0xD0: {
                    decompressCodeD(compressedData, byteArray);
                    break;
                }
            }
        }
    }

    private  void decompressCodeF(byte[] compressedData, byte[] byteArray, int numOfReadBytes)
    {
        int j = _uncompIndex;
        int seqLen = findSequenceLength(compressedData);
        _compIndex++;
        int bit = 0;

        while ((seqLen > 0) && (_compIndex < numOfReadBytes))
        {
            int val = ((1 << bit) & compressedData[_compIndex]);
            int val1 = (val > 0) ? 1 : 0;

            if (j > _uncompIndex) //if not the first element - start to equal between elements
                if (val1 != byteArray[j - 1]) {
                    seqLen--;
                }

            if (seqLen > 0) {
                byteArray[j] = (byte) val1;
                j++;
            }
            bit++;

            if (bit == 8) { //start passing on a new byte
                _compIndex++;
                bit=0;
            }
        }
        if (bit > 0) //if bit = 0 already did the _compIndex++
            _compIndex++;

        _uncompIndex = j;
        _crntValue = 1 - byteArray[j-1];
    }

    private  void  decompressCodeD(byte[] compressedData, byte[] byteArray)
    {
        //in this case, only need translate the seqLen to bytes according to the crnval
        int seqLen = findSequenceLength(compressedData);

        for (int i = _uncompIndex; i < _uncompIndex + seqLen; i++)
        {
            byteArray[i] = (byte)_crntValue;
        }

        _crntValue = 1 - _crntValue;
        _compIndex++;
        _uncompIndex += seqLen;
    }

    private int findSequenceLength(byte[] compressedData)
    {
        int count = compressedData[_compIndex] & 0xF;
        int seqLen = 0;

        if (count == 1) //the seqLen is maximum in length of 255, which takes maximum 1 bytes
        {
            _compIndex++;
            seqLen = ((int)(compressedData[_compIndex])) & 0xFF;
        }
        else if (count == 2) //the seqLen is maximum in length of 65535, which takes takes maximum 2 bytes
        {
            _compIndex += 2;
            seqLen = (((int)(compressedData[_compIndex])) & 0xFF) + ((((int)(compressedData[_compIndex-1])) &0xFF) << 8);
        }
        else if (count == 3) //the seqLen greater than 65535, which takes takes 3 bytes
        {
            _compIndex +=3;
            seqLen = (((int)(compressedData[_compIndex])) & 0xFF)  + ((((int)(compressedData[_compIndex-1])) &0xFF) << 8) +
                    ((((int)(compressedData[_compIndex-2])) & 0xFF) << 16);
        }

        return seqLen;
    }
}