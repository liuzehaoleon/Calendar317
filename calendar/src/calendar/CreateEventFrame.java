package calendar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;
import javax.swing.*;

/**
 * A JFrame responsible for prompting the user to enter the event information.
 */
public class CreateEventFrame extends JFrame {
	
	/**
	 * Constructs a CreateView object.
	 * @param month takes month from the ControllView to get current date and fill in TextField
	 */
	public CreateEventFrame(SelectDays month) {
		
        // Construct enclosing panel as Grid
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		panel.setLayout(new GridLayout(10, 2));
		
        // Construct all Labels and TextFields
		JLabel titleLbl = new JLabel("Title:");
		JLabel startLbl = new JLabel("Start Time (24h):");
		JLabel endLbl = new JLabel("End Time (24h):");
		JLabel dateLbl = new JLabel("Date:");
		JLabel priLbl = new JLabel("priority");
		
		JTextField title = new JTextField(7);
		JTextField start = new JTextField(7);
		JTextField end = new JTextField(7);
		JTextField dateTxt = new JTextField(7);
		JTextField priTxt = new JTextField(7);
		
		JButton cancel = new JButton("Cancel");
		JButton save = new JButton("Save");
		
        // Add cancel action
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
        
        // Add Save action, check for conflict and save to Event model
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				String titleString = title.getText();
				LocalDate date = LocalDate.parse(dateTxt.getText(), Calendar.formatter);
				LocalTime startTime = LocalTime.parse(start.getText());
				LocalTime endTime = LocalTime.parse(end.getText());
				int priority = Integer.parseInt(priTxt.getText()); 
				
				Event newEvent = new Event(titleString, date, startTime, endTime, priority);
				if(newEvent.conflicts()){
					new AlertView();
				} else {
					SimpleCalendar.calendar.add(newEvent);
					dispose();
				}
				}catch (Exception e1) {
					System.out.println("There is event conflict happened");
				};
			}		
		});
        
        // Adjust month to two characters
		String monthString = "";
		if(month.date.getMonthValue() < 10){
			monthString = "0"+month.date.getMonthValue(); 
		} else {
			monthString = String.valueOf(month.date.getMonthValue());
		}
        
		// Adjust day to two characters
		String dayString = "";
		if(month.date.getDayOfMonth() < 10){
			dayString = "0"+month.date.getDayOfMonth(); 
		} else {
			dayString = String.valueOf(month.date.getDayOfMonth());
		}
		
        // set date TextField and add all Labels and TextFields
		dateTxt.setText(monthString+"/"+dayString+"/"+month.date.getYear());
		panel.add(titleLbl);
		panel.add(title);
		panel.add(startLbl);
		panel.add(start);
		panel.add(endLbl);
		panel.add(end);
		panel.add(dateLbl);
		panel.add(dateTxt);
		panel.add(priLbl);
		panel.add(priTxt);
		panel.add(cancel);
		panel.add(save);
		
		add(panel);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
	}
}

final class AlertView extends JFrame{	
	/**
	 * Constructs an AlertView object.
	 */
	public AlertView() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JLabel label = new JLabel("The event you are trying to add conflicts with an existing event.");
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel.add(label);
		panel.add(ok);
		add(panel);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
	}
}