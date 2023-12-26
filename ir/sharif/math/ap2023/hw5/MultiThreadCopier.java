
package ir.sharif.math.ap2023.hw5;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class MultiThreadCopier {
    public static final long SAFE_MARGIN = 6;
    protected RandomAccessFile file;
    protected SourceProvider sourceProvider;
    protected WorkerManager workerManager;
    private int workerCount;

    public MultiThreadCopier(SourceProvider sourceProvider, String dest, int workerCount) {

        this.sourceProvider = sourceProvider;
        this.workerCount = workerCount;

        try {
            file = new RandomAccessFile(dest, "rws");
            file.setLength(sourceProvider.size());
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.workerManager = new WorkerManager(workerCount, sourceProvider, dest);
    }

    public void start()
    {
        workerManager.start();
    }


    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }
}
