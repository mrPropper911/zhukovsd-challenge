package by.belyahovich.run;

public class MoveCounter {
    private long count;
    private static volatile MoveCounter instance;

    public static MoveCounter getInstance(){
        MoveCounter localInstance = instance;
        if (localInstance == null){
            synchronized (MoveCounter.class){
                localInstance = instance;
                if (localInstance == null){
                    instance = localInstance = new MoveCounter();
                }
            }
        }
        return localInstance;
    }
}
