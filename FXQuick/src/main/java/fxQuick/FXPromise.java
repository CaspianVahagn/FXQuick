package fxQuick;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.concurrent.*;

public class FXPromise<T> {
    SimpleObjectProperty<Exception> errorProperty = new SimpleObjectProperty<>(null);
    SimpleObjectProperty<T> simpleObjectProperty = new SimpleObjectProperty<>(null);
    private ExecutorService service = Executors.newSingleThreadExecutor(r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });

    /**
     * Runs a callable function asynchronously.
     *
     * @param fun :the callable function
     * @return returns a FXPromise<T> object. call await() to work with called object
     * when ready
     */
    public FXPromise<T> async(Callable<T> fun) {

        Thread thread = new Thread(() -> {
            try {
                simpleObjectProperty.set(fun.call());
            } catch (Exception e) {
                System.out.println("Entered the Matrix");
                errorProperty.set(e);
            }

        });
        thread.setDaemon(true);
        thread.start();
        return this;

    }

    @SuppressWarnings("unchecked")
    public FXPromise<T> async(long timeout, Callable<T> fun) {

        Thread thread = new Thread(() -> {
            Future<?> future = service.submit(fun);
            try {
                simpleObjectProperty.set((T) future.get(timeout, TimeUnit.MILLISECONDS));
                service.shutdownNow();
            } catch (TimeoutException e) {
                e= new TimeoutException("Time out after " + timeout + " ms");
                future.cancel(true);
                errorProperty.set(e);
                service.shutdownNow();
            } catch (InterruptedException e) {
                future.cancel(true);
                errorProperty.set(e);
                service.shutdownNow();
            } catch (ExecutionException e) {
                future.cancel(true);
                errorProperty.set(e);
                service.shutdownNow();
            }
        });
        thread.setDaemon(true);
        thread.start();
        return this;
    }

    /**
     * awaits a result for the asynchronously called function. executes the promised
     * function when result is ready. Function will be queued in the FX Thread.
     *
     * @param promise
     */
    public void await(FXPromiseCallback<T> promise) {

        if (simpleObjectProperty.get() != null) {
            promise.awaitExecution(simpleObjectProperty.get());
            service.shutdownNow();
        } else {
            simpleObjectProperty.addListener(new ChangeListener<T>() {
                @Override
                public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                    Platform.runLater(() -> {
                        service.shutdownNow();
                        promise.awaitExecution(newValue);
                    });
                }
            });
        }
    }

    public FXPromise<T> onError(FXOnError onError){
        if (errorProperty.get() != null) {
            onError.handleError(errorProperty.get());
            service.shutdownNow();
        }else {
            errorProperty.addListener(new ChangeListener<Exception>() {
                @Override
                public void changed(ObservableValue<? extends Exception> observable, Exception oldValue, Exception newValue) {
                    Platform.runLater(() -> {
                        service.shutdownNow();
                        onError.handleError(newValue);
                    });
                }
            });
        }
        return this;
    }

    public void await(FXPromiseCallBackExcept<T> promise) {
        if (errorProperty.get() != null) {
            promise.awaitExecution(errorProperty.get(), null);
            service.shutdownNow();
        } else {
            errorProperty.addListener(new ChangeListener<Exception>() {

                @Override
                public void changed(ObservableValue<? extends Exception> observable, Exception oldValue, Exception newValue) {
                    Platform.runLater(() -> {
                        service.shutdownNow();
                        promise.awaitExecution(newValue, null);
                    });
                }
            });
        }
        if (simpleObjectProperty.get() != null) {
            promise.awaitExecution(errorProperty.get(), simpleObjectProperty.get());
        } else {
            simpleObjectProperty.addListener(new ChangeListener<T>() {

                @Override
                public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                    Platform.runLater(() -> {
                        service.shutdownNow();
                        promise.awaitExecution(errorProperty.get(), newValue);
                    });

                }
            });
        }
    }

    public interface FXPromiseCallback<T> {
        void awaitExecution(T param);
    }

    public interface FXPromiseCallBackExcept<T> {
        void awaitExecution(Exception err, T param);
    }

    public interface FXOnError{
        void handleError(Exception err);
    }

}