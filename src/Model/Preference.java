package Model;

public interface Preference {
	public boolean canChangePref();
	public void startSooner(int startHour) throws Exception;
	public void startSame();
	public void startLater(int startHour) throws Exception;
	public void Homework(int startHour) throws Exception;
    }

    


