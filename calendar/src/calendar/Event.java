package calendar;

import java.time.*;
import java.io.*;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Represents a calendar event either created by the user or loaded from a text file.  
 */
public class Event implements Comparable<Event>{

	//instance variables
	private LocalDate date;
	private String title="New Event";
	private LocalTime startTime;
	private LocalTime endTime;
	private File f = new File("./events.txt");
	private int priority = 2; //0-4
	
	/**
	 * Event constructor 
	 * @param title takes a String
	 * @param date takes a LocalDate
	 * @param startTime takes a LocalTime
	 * @param endTime takes a LocalTime
	 */
	public Event(String title, LocalDate date, LocalTime startTime, LocalTime endTime, int priority) {
		this.title = title;
		this.date = date;
		this.startTime = startTime;
		if(!(endTime == null))
			this.endTime = endTime;
		this.priority=priority;
	}
	
	/**
	 * All-day holiday Event constructor 
	 * @param title takes a String
	 * @param date takes a LocalDate
	 */
	public Event(String title, LocalDate date)
	{
		this.title = title;
		this.date = date;
		
		LocalTime currentTime=LocalTime.now();
		this.startTime = currentTime;
		this.endTime= currentTime.plusHours(1);
//		this.endTime = LocalTime.parse("23:59");
	}
	
	public Event(String title, LocalDate date, int priority)
	{
		this.title = title;
		this.date = date;
		this.priority = priority;
		
		LocalTime currentTime=LocalTime.now();
		this.startTime = currentTime;
		this.endTime= currentTime.plusHours(1);
//		this.endTime = LocalTime.parse("23:59");
	}

	/**
	 * No args constructor 
	 */
	public Event(){
		LocalTime currentTime=LocalTime.now();
		this.startTime = currentTime;
		this.endTime= currentTime.plusHours(1);
	}

	/** 
	 * Implements comparable interface from type Event
	 * @param Event acts as that to compare to this
	 * @return -1,0 or 1 depending if bigger, equal, or smaller respectively
	 */
	public int compareTo(Event event)
	{
		int dateCmp = this.date.compareTo(event.date);
		if (dateCmp != 0) // if date not equal
			return dateCmp; // return date comp
		int startTimeCmp = this.startTime.compareTo(event.startTime);
		if(startTimeCmp != 0) // if start time not equal
			return startTimeCmp; // return start time comp
		return this.title.compareTo(event.title); // if same date and same start time order by title
	}

	/**
	 * implements comparable's equals method with the help of compareTo
	 * @param Object takes AnyObject but then casts it as Event
	 * @return true if this and m are equal false if not
	 */
	public boolean equals(Object e)
	{
		return this.compareTo((Event)e) == 0;
	}

	/**
	 * loadEvents method used to load events from events.txt into memory
	 * @return ArrayList of type Event
	 */
	public ArrayList<Event> loadEvents(){
		ArrayList<Event> result = new ArrayList<>();
		if(!(f.exists())){
			System.out.println("File 'events.txt' does not exists. This is expected if you haven't saved any events yet.");
			return null;
		}
		try{
			// Chain or Readers
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			EventReader eventReader = new EventReader(br);

			while(true){
				Event e = eventReader.readEvent();
				if(e==null)
					break;
				result.add(e);
			}

			br.close();
			fr.close();
		}
		catch(IOException x)
		{
			System.out.println(x.getMessage());
		}
		System.out.println("Successfully loaded "+result.size()+" event(s).");
		return result;
	}

	/**
	 * saveEvents method which saves events from memory to events.txt
	 * @param events takes ArrayList of type Event
	 */
	public void saveEvents(ArrayList<Event> events){
		try
		{
			// Chain of writers.
			FileWriter fw = new FileWriter(f);
			PrintWriter pw = new PrintWriter(fw);
			EventWriter eventWriter = new EventWriter(pw);

			for (Event event : events){
				eventWriter.writeEvent(event);
			}
			pw.close();
			fw.close();
		}
		catch (IOException x)
		{
			System.out.println(x.getMessage());
		}
	}


	/**
	 * Returns the title of the event.
	 * @return title of Event as String
	 */
	public String getTitle(){
		return this.title;
	}

	/**
	 * Returns the date of the event.
	 * @return date of Event as String
	 */
	public String getDateStr(){
		int month = this.date.getMonthValue();
		String zeroMonth = "";
		if(month<10)
			zeroMonth = "0";
		int day = this.date.getDayOfMonth();
		String zeroDay = "";
		if(day<10)
			zeroDay = "0";
		int year = this.date.getYear();
		return (zeroMonth+month+"/"+zeroDay+day+"/"+year);
	}
	
	public LocalDate getDate() {
		return this.date;
	}

	public String getStartTimeStr() {
		return this.startTime.toString();
	}
	
	public String getEndTimeStr() {
		return this.endTime.toString();
	}
	
	public LocalTime getTime() {
		return this.startTime;
	}
	
	public LocalTime getEndTime() {
		return this.endTime;
	}
	
	public int getPriorty() {
		return this.priority;
	}
	

	/**
	 * hashCode method creates hash code for Event from title and start time
	 */
	public int hashCode()
	{
		return title.hashCode() + startTime.getHour();
	}

	/**
	 * sort method sorts events ArrayList with help of a TreeSet
	 */
	public static void sort()
	{
		//return sorted ArrayList, sorted with help of a TreeSet
		Calendar.events = new ArrayList<Event>(new TreeSet<Event>(Calendar.events));
	}

	/**
	 * Checks if the event conflicts with a pre-existing event in the calendar.
	 * @return true or false depending if Event conflicts with existing events in Calendar
	 */
	public boolean conflicts(){
		Boolean result = false;
		for (Event event : Calendar.events){
			if(event.date.equals(this.date)){
				if(this.startTime.isBefore(event.endTime) && this.endTime.isAfter(event.startTime)){
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * Returns a String representation of the event in the following format: 
	 * Title Date startingTime - EndingTime
	 * Example: Test Event 08/01/2017 12:00 - 14:00
	 * @return the String representation
	 */
	public String toString(){
		return getTitle() + " " + getDateStr() + " " + getStartTimeStr()
		+ "-" + getEndTimeStr();
	}
	
	/**
	 * Returns a String representation of a holiday event in the following format:
	 * Title Date
	 * Example: Test Event 08/01/2017 
	 * @return the String representation
	 */
	public String holidayToString(){
		return getTitle() + " " + getDateStr();
	}
}

class EventReader {
	
	private BufferedReader br;
	
	/**
	 * EventReader constructor
	 * @param br takes a BufferedReader which is constructed from a FileReader
	 */
	public EventReader(BufferedReader br){
		this.br = br;
	}
	
	/**
	 * readEvent method which reads event from BufferedReader 
	 * @return Event which can then be processed 
	 * @throws IOException if FileReader exception occurs
	 */
	public Event readEvent() throws IOException {
		String singleEvent=br.readLine();
		if (singleEvent==null)
			return null;
		String[] splitor=singleEvent.split("\\|");
		String title = splitor[0];
		LocalDate date = LocalDate.parse(splitor[1], Calendar.formatter);
		LocalTime startTime = LocalTime.parse(splitor[2]);
		LocalTime endTime = LocalTime.parse(splitor[3]);
		int priority= Integer.parseInt(splitor[4]); 
		return new Event(title, date, startTime, endTime, priority);
	}
}

class EventWriter {

	private PrintWriter printWriter;
	
	/**
	 * EventWriter constructor 
	 * @param printWriter takes PrintWriter
	 */
	public EventWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}
	
	/**
	 * writeEvent method which writes an event to a printWriter.
	 * @param event takes Event to write
	 * @throws IOException 
	 */
	public void writeEvent(Event event) throws IOException{
		printWriter.println(event.getTitle()+ '|'+ event.getDateStr()+ "|"+
			event.getStartTimeStr()+"|"+ event.getEndTime()+event.getPriorty()+"|");
	}
}
