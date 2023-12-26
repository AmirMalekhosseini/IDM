package ir.sharif.math.ap2023.hw5;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class WorkerThread extends Thread {

    private volatile long startPosition = 0;
    private volatile long endPosition = 0;
    private volatile long byteWrittenByWorker = 0;
    private volatile long remainingWork;
    private boolean isWorkerRunning = true;
    private boolean workerStart = true;
    protected SourceProvider sourceProvider;
    protected SourceReader sourceReader;
    protected RandomAccessFile file;
    protected String path;
    protected CalculateWork calculateWork;
    protected WorkerManager workerManager;

    public WorkerThread(SourceProvider sourceProvider, String  path) {
        this.path = path;
        this.sourceProvider = sourceProvider;
        try {
            file = new RandomAccessFile(path, "rws");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void run()
    {

        byteWrittenByWorker = 0;

            while (isWorkerRunning) {
                writeToFile();
                calculateWork.getDoneWorker(this);
                byteWrittenByWorker = 0;
            }

        try {
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToFile()
    {
        try {
            if (workerStart) {
                file.seek(startPosition);
                sourceReader = sourceProvider.connect(startPosition);
                workerStart = false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        while (remainingWork > 0) {
            byte readByte = sourceReader.read(); ;

            try {
                file.write(readByte);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byteWrittenByWorker++;
            remainingWork--;
        }

    }

    public void addWorkerManager(WorkerManager workerManager) {
        this.workerManager = workerManager;
    }

    public void addCalculateWork(CalculateWork calculateWork) {
        this.calculateWork = calculateWork;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(long endPosition) {
        this.endPosition = endPosition;
    }

    public long getByteWrittenByWorker() {
        return byteWrittenByWorker;
    }

    public void setByteWrittenByWorker(long byteWrittenByWorker) {
        this.byteWrittenByWorker = byteWrittenByWorker;
    }

    public long getRemainingWork() {
        return remainingWork;
    }

    public void setRemainingWork(long remainingWork) {
        this.remainingWork = remainingWork;
    }

    public boolean isWorkerRunning() {
        return isWorkerRunning;
    }

    public void setWorkerRunning(boolean workerRunning) {
        isWorkerRunning = workerRunning;
    }

    public boolean isWorkerStart() {
        return workerStart;
    }

    public void setWorkerStart(boolean workerStart) {
        this.workerStart = workerStart;
    }


}
