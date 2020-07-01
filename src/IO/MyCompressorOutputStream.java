package IO;

import algorithms.mazeGenerators.Maze;

import java.io.IOException;
import java.io.OutputStream;

public class MyCompressorOutputStream extends OutputStream {
    private OutputStream out;
    private byte[] _compressedData;
    private int _compIndex;

    public MyCompressorOutputStream(OutputStream o) {
        out = o;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] byteArray) throws IOException {
        // Initialize sequence array, holding sequences lengths
        int[] sequence = new int[byteArray.length];
        // Write 12 bytes of header
        writeHeader(byteArray);
        // Write to file the first value
        write( byteArray[Maze.mazeHeaderLen] );
        //fill the sequence, return the number of the sequences
        int seqCount = findSequence(byteArray, sequence);
        //compress the date
        compressData(byteArray, sequence, seqCount);

        // Write compressed data to filr
        for (int i = 0; i < _compIndex; i++)
            write(_compressedData[i]);
    }

    private void writeHeader(byte[] byteArray) throws IOException {
        for (int i = 0; i < Maze.mazeHeaderLen; i++)
            write(byteArray[i]);

    }

    private void compressData(byte[] byteArray, int[] sequence, int seqCount) throws IOException
    {
        int indx = 0;
        int crntval = byteArray[Maze.mazeHeaderLen];
        _compIndex = 0;
        _compressedData = new byte[byteArray.length];

        while (indx < seqCount) {
            int tmpSeqCnt = 0;
            int firstIndx = indx;

            while ((sequence[indx] <= 16) && (indx < seqCount)) { //check how many sequences have a length that is less than 16
                tmpSeqCnt++; //represents in which index it starts
                indx++;
            }
            if (tmpSeqCnt > 0) //if true - there are sequences of length less than 16, so compress them with format F
                formatSequenceF(sequence, firstIndx, tmpSeqCnt, crntval);

            // Update crntVal
            if ((tmpSeqCnt % 2) == 1)
                crntval = 1 - crntval;

            if (indx < seqCount) {
                formatSequenceD(sequence[indx]); //there is a sequence that its length is more than 16, compress if with format D
                crntval = 1 - crntval;
            }
            indx++;
        }
    }


    private int findSequence(byte[] byteArray, int[] sequence)
    {
        //build the sequences array
        int location = Maze.mazeHeaderLen; // first byte of data
        int seqCount = 0;
        int crntVal = byteArray[location];

        sequence[0] = 0;

        while (location < byteArray.length) {
            if (crntVal == byteArray[location]) {
                sequence[seqCount]++;
            } else {
                seqCount++;
                sequence[seqCount] = 1; //assume that there is at least one element in the sequence
                crntVal = byteArray[location];
            }
            location++;
        }

        seqCount++;

        return seqCount; // Number of valid sequences in sequence array
    }


    private void formatSequenceF(int[]sequence, int firstIndx, int tmpSeqCnt, int firstVal) throws IOException
    {
        int crntVal = firstVal;
        int bitNum = 0;
        writeSequenceCountFormatF(tmpSeqCnt);
        _compressedData[_compIndex] = 0;

        //firstIndx represents in which index in the sequence array need to begin
        //tmpSeqCnt represents how many sequences need to pass
        for (int i = firstIndx; i < (firstIndx + tmpSeqCnt); i++)
        {
            for (int j =0; j < sequence[i]; j++){
                // Need to write only 1 (compresses data is already initialize with 0)
                if (crntVal == 1) {
                    byte val = (byte) (1 << bitNum);
                    _compressedData[_compIndex] |= val; //or - for saving the rest of bytes
                }
                bitNum++;
                if (bitNum == 8){ //end of the current byte, continue to the next byte
                    bitNum = 0;
                    _compIndex++;
                    _compressedData[_compIndex] = 0;
                }
            }
            crntVal = 1 - crntVal;
        }

        // In last _compressedData[_compIndex], rest of byte should be opposite than last value
        writeRestBytesFormatF(bitNum, crntVal);
    }

    private void writeSequenceCountFormatF(int tmpSeqCnt)
    {
        // Write code + how many sequences
        if (tmpSeqCnt < 256){ // 2^8 => 1 byte
            _compressedData[_compIndex]   = (byte)0xF1;
            _compressedData[_compIndex+1] = (byte)tmpSeqCnt;
            _compIndex += 2;
        }
        else if (tmpSeqCnt < 65536) { // 2^16 => 2 bytes
            _compressedData[_compIndex]   = (byte)0xF2;
            _compressedData[_compIndex+1] = (byte)((tmpSeqCnt & 0xFF00) >> 8);
            _compressedData[_compIndex+2] = (byte)(tmpSeqCnt & 0xFF);
            _compIndex += 3;
        }
        else{ // 3 bytes, until 2^24 = 16M
            _compressedData[_compIndex]   = (byte)0xF3;
            _compressedData[_compIndex+1] = (byte)((tmpSeqCnt & 0xFF0000) >> 16);
            _compressedData[_compIndex+2] = (byte)((tmpSeqCnt & 0xFF00) >> 8);
            _compressedData[_compIndex+3] = (byte)(tmpSeqCnt & 0xFF);
            _compIndex += 4;
        }
    }

    private void writeRestBytesFormatF(int bitNum, int crntVal)
    {
        if (bitNum > 0){
            if (crntVal == 1) {  //so need to fill the other with 1 because the last loop passing updated the crntval from 0 to 1
                while (bitNum < 8) {
                    byte val = (byte) (1 << bitNum);
                    _compressedData[_compIndex] |= val;
                    bitNum++;
                }
            }
            _compIndex++;
        } //if crntval is 0 (before update was 1) need to do nothing because the byte already initialize with 0
        else{ //bitNum is exactly 0
            // Write complete byte with different value, so reader can know that this is the last sequence
            if (crntVal == 1)
                _compressedData[_compIndex] = (byte)0xFF;
            else
                _compressedData[_compIndex] = 0;
            _compIndex++;
        }
    }

    private void formatSequenceD(int seqVal)
    {
        // Write code + sequence value
        if (seqVal < 256) { // 2^8 => 1 byte
            _compressedData[_compIndex]   = (byte)0xD1;
            _compressedData[_compIndex+1] = (byte)seqVal;
            _compIndex += 2;
        }
        else if (seqVal < 65536) { // 2^16 => 2 bytes
            _compressedData[_compIndex]   = (byte)0xD2;
            _compressedData[_compIndex+1] = (byte)((seqVal & 0xFF00) >> 8);
            _compressedData[_compIndex+2] = (byte)(seqVal & 0xFF);
            _compIndex += 3;
        }
        else { // 3 bytes, until 2^24 = 16M
            _compressedData[_compIndex]   = (byte)0xD3;
            _compressedData[_compIndex+1] = (byte)((seqVal & 0xFF0000) >> 16);
            _compressedData[_compIndex+2] = (byte)((seqVal & 0xFF00) >> 8);
            _compressedData[_compIndex+3] = (byte)(seqVal & 0xFF);
            _compIndex += 4;
        }
    }

}