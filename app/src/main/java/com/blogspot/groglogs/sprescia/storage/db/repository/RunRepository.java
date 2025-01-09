package com.blogspot.groglogs.sprescia.storage.db.repository;

import android.app.Application;

import com.blogspot.groglogs.sprescia.model.entity.RunItem;
import com.blogspot.groglogs.sprescia.storage.db.AppDatabase;
import com.blogspot.groglogs.sprescia.storage.db.dao.RunDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RunRepository {

    private final RunDao runDao;

    public RunRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        runDao = db.fuelDao();
    }

    public RunItem findById(Long id) throws ExecutionException, InterruptedException {
        return AppDatabase.getDatabaseWriteExecutor().submit(() -> runDao.findById(id)).get();
    }

    public List<RunItem> getAllItemsByDateDesc() throws ExecutionException, InterruptedException {
        return AppDatabase.getDatabaseWriteExecutor().submit(runDao::getAllItemsByDateDesc).get();
    }

    public long insert(RunItem entity) throws ExecutionException, InterruptedException {
        return AppDatabase.getDatabaseWriteExecutor().submit(() -> runDao.insert(entity)).get();
    }

    public void delete(long id) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> runDao.delete(id));
    }

    public void deleteAll() {
        AppDatabase.getDatabaseWriteExecutor().execute(runDao::deleteAll);
    }

    public void update(RunItem entity) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> runDao.update(entity));
    }
}
