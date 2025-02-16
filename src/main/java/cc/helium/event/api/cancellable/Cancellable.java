package cc.helium.event.api.cancellable;

/**
 * An interface for objects that can be cancelled.
 * Implementing classes can indicate whether they are cancelled or not using the provided methods.
 * <p>
 * regarded as package public...
 */
interface Cancellable {

    /**
     * Checks if the object is cancelled.
     *
     * @return {@code true} if the object is cancelled, {@code false} otherwise.
     */
    boolean isCancelled();

    /**
     * Sets the cancellation state of the object.
     *
     * @param state {@code true} to cancel the object, {@code false} to uncancel it.
     */
    void setCancelled(boolean state);
}
