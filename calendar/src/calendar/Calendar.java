package calendar;

import java.util.*;
import java.time.*;
import java.time.format.*;
import javax.swing.event.*;

/**
 * A class that keeps track of all created events and the dates on which they are
 * scheduled. It serves as the main model of the project.
 */

public class Calendar {
	
	//instance variables
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	public static ArrayList<Event> events = new ArrayList<>(); // store events here while running
	public static LocalDate today = LocalDate.now( SimpleCalendar.z );
	private ArrayList<ChangeListener> listeners;

	/**
	 * Constructs a Calendar object.
	 * @param e takes an ArrayList of events, can be empty
	 */
	public Calendar(ArrayList<Event> e){
		events = e;
		listeners = new ArrayList<ChangeListener>();
	}

	/**
	 * Attaches a listener to the Model
	 * @param c the listener
	 */
	public void attach(ChangeListener c){
		listeners.add(c);
	}

	/**
	 * Adds new data to the model
	 * @param s takes a String to add
	 */
	public void add(Event e){
		events.add(e);
		for (ChangeListener l : listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}
	
	/**
	 * Remove a event given a title
	 * @param title the given title
	 * @return true if the given title matches any scheduled events
	 */
	public boolean remove(String title){
		for (Event e : events){
			if (e.getTitle().equals(title)){
				events.remove(e);
				return true;
			}
		}
		return false;
	}


	/**
	 * Checks for events on a given date.
	 * @param date the given date
	 * @return true if the given date has any scheduled events
	 */
	public boolean hasEvents(LocalDate date){
		for (Event e : events)
		{
			if (e.getDate().equals(date))
				return true;
		}
		return false;
	}
	
	/**
	 * Gets an ArrayList of events scheduled on a given date.
	 * @param date the given date  
	 * @return an ArrayList with the events information
	 */
	public ArrayList<Event> getEventsOnThisDate(LocalDate date){
		ArrayList<Event> eventsOnThisDate = new ArrayList<>();
		for (Event e : events)
		{
			if (e.getDate().equals(date))
				eventsOnThisDate.add(e);
		}
		
		return eventsOnThisDate;
	}

}
