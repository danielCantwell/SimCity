package SimCity.Globals;

public class Singleton {
    private Singleton() {
        //lots of initialization code
    }

    private static class SingletonHolder { 
        public static final Singleton instance = new Singleton();
    }

    public static synchronized Singleton Get() {
        return SingletonHolder.instance;
    }
}
