package fxQuick;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class FXPromise<T> {
	private final static ExecutorService service = Executors.newFixedThreadPool(256, r -> {
		Thread t = Executors.defaultThreadFactory().newThread(r);
		t.setDaemon(true);
		return t;
	});

	public interface FXPromiseCallback<T> {

		public void awaitExecution(T param);

	}

	SimpleObjectProperty<T> simpleObjectProperty = new SimpleObjectProperty<>(null);

	/**
	 * Runs a callable function asycronously.
	 * 
	 * @param fun :the callable function
	 * @return returns a FXAsync object. call await() to work with called object
	 *         when ready
	 * 
	 */
	public FXPromise<T> async(Callable<T> fun) {

		service.execute(() -> {
			try {
				simpleObjectProperty.set(fun.call());
			} catch (Exception e) {

				e.printStackTrace();
			}
			System.out.println("DONE: " + Thread.currentThread().getName());
		});

		return this;

	}
	
	@SuppressWarnings("unchecked")
	public FXPromise<T> async(long timeout, Callable<T> fun) {
		
		service.execute(() -> {
			Future<?> future = service.submit(fun);
			try {
				
				
				simpleObjectProperty.set((T)future.get(timeout, TimeUnit.MILLISECONDS));
				System.out.println("DONE: " + Thread.currentThread().getName());
			} catch (Exception e) {
				future.cancel(true);
				System.out.println("CANCELED");
			}
			
		});

		return this;

	}

	/**
	 * awaits a result for the asyncronously called function. executes the promised
	 * function when result is ready. Function will be queued in the FX Thread.
	 * 
	 * @param promise
	 */
	public void await(FXPromiseCallback<T> promise) {

		if (simpleObjectProperty.get() != null) {
			promise.awaitExecution(simpleObjectProperty.get());
		} else {
			simpleObjectProperty.addListener(new ChangeListener<T>() {

				@Override
				public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
					Platform.runLater(() -> {

						promise.awaitExecution(newValue);

					});

				}
			});
		}
	}
}