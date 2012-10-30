package alice.util;

public class VersionInfo 
{
	private static final String ENGINE_VERSION = "2.5";
	private static final String JAVA_SPECIFIC_VERSION = "1";
	private static final String NET_SPECIFIC_VERSION = "0";
	private static final String ANDROID_SPECIFIC_VERSION = "1";
	
	public static String getEngineVersion()
	{
		return ENGINE_VERSION;
	}
	
	public static String getPlatform()
	{
		String vmName = System.getProperty("java.vm.name");
		if(vmName.contains("Java")) //"Java HotSpot(TM) Client VM"
			return "Java";
		else if(vmName.equals("IKVM.NET"))
			return ".NET";
		else if(vmName.equals("Dalvik"))
			return "Android";
		else 
			throw new RuntimeException();
	}
	
	public static String getSpecificVersion()
	{
		String vmName = System.getProperty("java.vm.name");
		if(vmName.contains("Java")) //"Java HotSpot(TM) Client VM"
			return JAVA_SPECIFIC_VERSION;
		else if(vmName.equals("IKVM.NET"))
			return NET_SPECIFIC_VERSION;
		else if(vmName.equals("Dalvik"))
			return ANDROID_SPECIFIC_VERSION;
		else
			throw new RuntimeException();
	}
	
	public static String getCompleteVersion()
	{
		return getEngineVersion() + "." + getSpecificVersion();
	}
}
