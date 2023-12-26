package ir.sharif.math.ap2023.hw5;

import java.util.ArrayList;

public class CalculateWork {

    protected ArrayList<WorkerThread> allWorkers;
    protected SourceProvider sourceProvider;

    public CalculateWork(WorkerManager workerManager, SourceProvider sourceProvider) {

        this.sourceProvider = sourceProvider;
        this.allWorkers = workerManager.allWorkerThreads;

    }

    public void returnAllWorkers() {
        setWorks();
    }

    public void setWorks() {

        setStartPosition();
        setEndPosition();
        for (WorkerThread allWorker : allWorkers) {
            calculateWork(allWorker);
            allWorker.addCalculateWork(this);
        }

    }

    public synchronized void getDoneWorker(WorkerThread doneWorker) {
        WorkerThread unDoneWorker = calculateExtraWork();

        if (unDoneWorker == null || unDoneWorker.getRemainingWork() < 6) {
            doneWorker.setWorkerRunning(false);
            return;
        }

        allocateExtraWork(doneWorker, unDoneWorker);
    }

    public WorkerThread calculateExtraWork() {

        long tempRemainingWork = 0;
        WorkerThread undoneWorker = null;

        for (WorkerThread allWorker : allWorkers) {

            if (allWorker.getRemainingWork() > tempRemainingWork) {
                tempRemainingWork = allWorker.getRemainingWork();
                undoneWorker = allWorker;
            }

        }

        return undoneWorker;
    }

    public synchronized void allocateExtraWork(WorkerThread doneWorker, WorkerThread unDoneWorker) {

        doneWorker.setStartPosition(unDoneWorker.getStartPosition() + unDoneWorker.getByteWrittenByWorker() + unDoneWorker.getRemainingWork() / 2);
        doneWorker.setRemainingWork(unDoneWorker.getRemainingWork() - (unDoneWorker.getRemainingWork() / 2));
        unDoneWorker.setRemainingWork(unDoneWorker.getRemainingWork() / 2);
        doneWorker.setWorkerStart(true);

    }

    public void calculateWork(WorkerThread workerThread) {

        long remainingWork = workerThread.getEndPosition() - workerThread.getStartPosition() + 1;
        workerThread.setRemainingWork(remainingWork);

    }

    public void setStartPosition() {

        for (int i = 0; i < allWorkers.size(); i++) {
            allWorkers.get(i).setStartPosition((int) (i * (sourceProvider.size()) / allWorkers.size()));
        }

    }

    public void setEndPosition() {

        for (int i = 0; i < allWorkers.size(); i++) {

            if (i == allWorkers.size() - 1) {
                allWorkers.get(i).setEndPosition(sourceProvider.size() - 1);
                break;
            }

            allWorkers.get(i).setEndPosition(allWorkers.get(i + 1).getStartPosition() - 1);
        }
    }

}
