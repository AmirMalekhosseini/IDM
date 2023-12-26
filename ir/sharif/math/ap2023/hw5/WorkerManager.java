package ir.sharif.math.ap2023.hw5;


import java.util.ArrayList;

public class WorkerManager extends Thread {

    protected ArrayList<WorkerThread> allWorkerThreads;
    protected CalculateWork calculateWork;
    protected SourceProvider sourceProvider;
    private int workerCount;

    public WorkerManager(int workerCount, SourceProvider sourceProvider, String path) {
        this.workerCount = workerCount;
        this.sourceProvider = sourceProvider;
        allWorkerThreads = new ArrayList<>();
        for (int i = 0; i < workerCount; i++) {
            WorkerThread newWorkerThread = new WorkerThread(sourceProvider, path);
            allWorkerThreads.add(newWorkerThread);
        }
    }

    public void run() {

        addWorkerManager();
        calculateWork = new CalculateWork(this, sourceProvider);
        startWorkers();

    }

    public void addWorkerManager() {
        for (WorkerThread allWorkerThread : allWorkerThreads) {
            allWorkerThread.addWorkerManager(this);
        }
    }

    public void startWorkers() {
        calculateWork.returnAllWorkers();
        for (WorkerThread allWorkerThread : allWorkerThreads) {
            allWorkerThread.start();
        }
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }
}
