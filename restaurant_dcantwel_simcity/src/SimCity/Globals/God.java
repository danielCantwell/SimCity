package SimCity.Globals;

public class God extends Singleton{
    private God() {
        //lots of initialization code
    }

    private static class SingletonHolder { 
        public static final God instance = new God();
    }

    public static synchronized God Get() {
        return SingletonHolder.instance;
    }
}
