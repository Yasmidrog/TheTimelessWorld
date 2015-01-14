package TheTimeless.game;


public interface IControlable {
    /**
     * Check pressed keys
     */
    void control(int delta);
    /**
     * Interact with some objects
     */
    void interact(int delta);
}
