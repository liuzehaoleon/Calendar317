package calendar;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

import java.awt.*;
import java.text.DateFormatSymbols;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A JPanel that represents the Calendar Month View in GUI. 
 */

public class MonthViewPanel extends JPanel implements ChangeListener {

	//instance variables
	private Calendar calendar;
	private textPanel[] textPanes;
	private LocalDate selectedDate;
	private JPanel gridPanel;

	/**
	 * Constructs a MonthView object.
	 * @param c a reference to the Calendar object (model)
	 */
	public MonthViewPanel(Calendar c)
	{
		calendar = c;
		selectedDate = SimpleCalendar.month.getSelectedDate();
		initializePanes();
		gridPanel = new JPanel();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	/**
	 * Changes the number of panes in the grid according to the currently selected month.
	 */
	private void initializePanes()
	{
		textPanes = new textPanel[selectedDate.lengthOfMonth()];
		for (int i = 0; i < textPanes.length; i++)
		{
			textPanes[i] = new textPanel();
			textPanes[i].setDayValue(" " + Integer.toString(i+1));
		}
	}
	
	/**
	 * Draws the drawView object.
	 */
	public void drawView()
	{
		//get current month
		java.time.Month monthEnum = selectedDate.getMonth(); 
		//create label with the current month
		JLabel monthLabel = new JLabel(monthEnum.toString() + " " + selectedDate.getYear());
		this.add(monthLabel);
		//create grid of month days
		createGridPanel(selectedDate);
		this.add(gridPanel);
		this.setPreferredSize(new Dimension(450,200));

		JScrollPane scrollPane = new JScrollPane(gridPanel);
		scrollPane.setPreferredSize(new Dimension(400,200));
		add(scrollPane);
	}

	/**
	 * Creates the individual month days.
	 * @param date the currently selected localDate
	 */
	public void createGridPanel(LocalDate date)
	{
		//create new grid with 7 columns and 0 gaps both horizontally and vertically
		gridPanel.setLayout(new GridLayout(0, 7, 0, 0));

		//create temporary calendar with DAY_OF_MONTH = 1 : the first day of the month
		LocalDate temp = LocalDate.of(date.getYear(), date.getMonthValue(), 1);

		DateFormatSymbols symbols = new DateFormatSymbols(SimpleCalendar.language);
		List<String> weekDays = Arrays.asList(symbols.getShortWeekdays());

		JPanel weekDaysPanel = new JPanel();
		weekDaysPanel.setLayout(new GridLayout(0, 7, 0, 0));
		//add labels with weekdays to the panel
		for (int day=1; day<8;day++)
		{
			JLabel label = new JLabel(weekDays.get(day));
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setPreferredSize(new Dimension(10,30));
			weekDaysPanel.add(label);
		}
		this.add(weekDaysPanel);

		//determine the name of the first day of month. 
		//getValue returns the int value of the enum
		int firstDay = temp.getDayOfWeek().getValue();

		//add blank JTextPanes till you reach the first day of the month
		for (int i = 0; i < firstDay; i++)
		{
			textPanel pane = new textPanel();
			gridPanel.add(pane);
		}

		for (textPanel pane : textPanes)
		{			
			pane.removeAll();
			pane.updateUI();
			//keep track of 'today' in the month view
			LocalDate today = LocalDate.now();
			LocalDate dummy = LocalDate.of(selectedDate.getYear(), selectedDate.getMonth().getValue(), Integer.parseInt(pane.getDayValueText().trim()));
			if (dummy.equals(today))
				pane.getDayValue().setForeground(Color.blue);
			else
				pane.getDayValue().setForeground(Color.black);
			pane.insertComponent(pane.getDayValue());
			gridPanel.add(pane);
		}

	}

	/**
	 * Display events scheduled on the currently selected day in the Month view.
	 * @param date the currently selected date 
	 */
	private void displayEvents(LocalDate date)
	{
		//change the number of the panes according to the currently selected month
		Arrays.fill(textPanes, null);
		initializePanes();
		
		//To keep track of events on that day
		ArrayList<Event> events = new ArrayList<>();

		//iterate over all the days in the month 
		for (int i = 0; i < date.lengthOfMonth(); i++)
		{
			//create a dummy LocalDate object with changing the monthDay each iteration
			LocalDate temp = LocalDate.of(date.getYear(), date.getMonthValue(),
					i+1);
			//check if there are any events scheduled on this dummy date
			if (calendar.hasEvents(temp))
				//if so, add all of them to the events ArrayList
				events.addAll(calendar.getEventsOnThisDate(temp));
		}

		//clear all textPanes from any pre-existing string
		for (textPanel p : textPanes)
		{
			p.setText("");
		}

		//iterate over all events scheduled on that day
		for (Event event : events)
		{
			//get the month day of the event 
			int monthDay = event.getDate().getDayOfMonth();

			//iterate over all textPanes
			for (textPanel pane : textPanes)
			{
				//trim white space before you assign it to the integer variable
				int dayValue = Integer.parseInt(pane.getDayValueText().trim());
				//if the pane's dayValue corresponds to the same monthDay as that of the event
				if (dayValue == monthDay){
					String text;

					text = pane.getText() + "\n" + event.getTitle() + " " + 
							event.getStartTimeStr() + " - " + event.getEndTime();
					pane.setForeground(Color.RED);
					
					//clear the pane
					pane.setText("");
					//display the new updated pane
					pane.setText(text);

					//pane.setBackground(Color.RED);
					break;
				}	
			}
		}
	}

	@Override
	/**
	 * Updates the MonthView upon a change in the model. 
	 * @param e the event that notifies the view of the change
	 */
	public void stateChanged(ChangeEvent e) {

		//remove any existing components in the MonthView panel
		this.removeAll();
		gridPanel.removeAll();
		this.updateUI();
		//get updated information from the model
		selectedDate = SimpleCalendar.month.getSelectedDate();
		//if the selected date has any scheduled events, display them
		if (!Calendar.events.isEmpty())
			displayEvents(selectedDate);

		//draw the day view
		drawView();
	}

	/**
	 * Gets the array of textPanes in the dayGUI.
	 * @return the array of textPanes
	 */
	public textPanel[] getTextPanes()
	{
		return textPanes;
	}
}

class textPanel extends JTextPane{
	
	//instance variables
	private JButton dayValue;
	private boolean flag;

	public textPanel()
	{
		this.setEditable(false);
		dayValue = new JButton();
		flag = false;
		dayValue.setBorder(new EmptyBorder(0,0,0,0));
		this.insertComponent(dayValue);
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		this.setPreferredSize(new Dimension(100,120));
	}
	
	public JButton getDayValue(){
		return dayValue;
	}
	
	public String getDayValueText(){
		return dayValue.getText();
	}
	
	public boolean getFlag(){
		return flag;
	}
	
	public void setFlag(boolean value){
		flag = value;
	}
	
	public void setDayValue(String s){
		dayValue.setText(s);
	}
}