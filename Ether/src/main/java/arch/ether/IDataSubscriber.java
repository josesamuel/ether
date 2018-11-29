package arch.ether;

/**
 * Subscribes to data of type T
 */
public interface IDataSubscriber<T> {

    /**
     * Called when the data is available
     */
    void onData(T data);
}
