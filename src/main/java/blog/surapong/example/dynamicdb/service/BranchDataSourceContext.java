package blog.surapong.example.dynamicdb.service;

public class BranchDataSourceContext {

    private BranchDataSourceContext() {}

    private static final ThreadLocal<String> currentThread = new ThreadLocal<>();

    public static void setCurrentBranchDataSourceId(String dataSourceId) {
        currentThread.set(dataSourceId);
    }

    public static String getCurrentBranchDataSourceId() {
        return currentThread.get();
    }

    public static void clearBranchContext() {
        currentThread.remove();
    }

}
