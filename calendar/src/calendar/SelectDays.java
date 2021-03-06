package calendar;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.text.DateFormatSymbols;

/**
 * Responsible for keeping track of the currently selected date and notifying the views.
 * Serves as part of the model of the project.
 */

public class SelectDays {

	//instance variables
	public LocalDate date;
	private ArrayList<ChangeListener> listeners;
	public LocalDate startDate;
    public LocalDate endDate;

	/**
	 * Constructor of Month object
	 * @param date takes a LocalDate which the whole month is centered around
	 */
	public SelectDays(LocalDate date) {
		this.date = date;
		this.startDate = date ;
	    this.endDate = date ;
		listeners = new ArrayList<ChangeListener>();
	}

	/**
	 * Attaches a listener to the Model
	 * @param c the listener
	 */
	public void attach(ChangeListener c) {
		listeners.add(c);
	}

	/**
	 * Changes the current date and notifies listeners.
	 * @param date the new date
	 */
	public void changeDate(LocalDate date) {
		this.date = date;
		for (ChangeListener l : listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}

	
	/**
	 * Returns an array of LocalDates, 42 to be exact which will be displayed in the Month view.
	 * @return the array of LocalDates 
	 */
	public LocalDate[] getDateArray() {
		LocalDate firstOfMonth = this.date.withDayOfMonth(1);
		LocalDate currentDay = firstOfMonth.minusDays(firstOfMonth.getDayOfWeek().getValue());
		LocalDate[] result = new LocalDate[42];
		for (int day = 0; day < 42; day++) {
			result[day] = currentDay;
			currentDay = currentDay.plusDays(1);
		}
		return result;
	}

	/**
	 * Will always be the current week 
	 * Weeks start on Sunday and end on Saturday
	 * @return an array of LocalDates 
	 * 
	 */
	public LocalDate[] getWeekArray() {
		LocalDate currentDate = this.date;
		LocalDate[] result = new LocalDate[7];
		for (int day = 0; day < 7; day++) {
			if (day == 0) {
				if (currentDate.getDayOfWeek().getValue() != 7) {
					currentDate = currentDate.minusDays(currentDate.getDayOfWeek().getValue());
				}
			}
			result[day] = currentDate;
			currentDate = currentDate.plusDays(1);
		}
		return result;
	}

	
	/**
	 * Short form of the weekdays in the language defined in the SimpleCalender class
	 * @return an array of Strings
	 */
	public String[] weekdays() {
		DateFormatSymbols symbols = new DateFormatSymbols(SimpleCalendar.language);
		return symbols.getShortWeekdays();
	}

	
	/**
	 * Returns a string representation of the month name and year
	 * Example: "July 2017"
	 * @return a String of month name and year
	 */
	public String monthYearString() {
		return this.date.getMonth().getDisplayName(TextStyle.FULL, SimpleCalendar.language) + " " + this.date.getYear();
	}
	
	/**
	 * Gets the currently selected date.
	 * @return the selected date
	 */
	public LocalDate getSelectedDate()
	{
		return date;
	}
	
}
