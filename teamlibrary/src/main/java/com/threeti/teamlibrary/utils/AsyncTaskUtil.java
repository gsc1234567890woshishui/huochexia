package com.threeti.teamlibrary.utils;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.threeti.teamlibrary.net.task.PriorityAsyncTask;

import java.util.Collection;

public class AsyncTaskUtil {
    public static boolean isAsyncTaskRunning(AsyncTask<?, ?, ?> task) {
        if (null != task && task.getStatus().equals(AsyncTask.Status.RUNNING)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAsyncTaskPending(AsyncTask<?, ?, ?> task) {
        if (null != task && task.getStatus().equals(AsyncTask.Status.PENDING)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAsyncTaskFinished(AsyncTask<?, ?, ?> task) {
        if (null == task || task.getStatus().equals(AsyncTask.Status.FINISHED) || task.isCancelled()) {
            return true;
        } else {
            return false;
        }
    }

    public static OnCancelListener defaultDialogInterfaceCancelListener(final AsyncTask<?, ?, ?> task) {
        if (null != task) {
            return new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    task.cancel(true);
                }
            };
        } else {
            return null;
        }
    }
    public static OnCancelListener defaultDialogInterfaceCancelListener(final PriorityAsyncTask<?, ?, ?> task) {
        if (null != task) {
            return new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    task.cancel(true);
                }
            };
        } else {
            return null;
        }
    }

    public static void cancelTask(AsyncTask<?, ?, ?> task) {
        if (isAsyncTaskRunning(task)) {
            task.cancel(true);
        }
    }

    public static void cancelTaskCollection(Collection<? extends AsyncTask<?, ?, ?>> tasks) {
        if (null != tasks) {
            for (AsyncTask<?, ?, ?> task : tasks) {
                cancelTask(task);
            }
        }
    }

    public static void cancelTasks(AsyncTask<?, ?, ?>... tasks) {
        if (null != tasks) {
            for (AsyncTask<?, ?, ?> task : tasks) {
                cancelTask(task);
            }
        }
    }
}
