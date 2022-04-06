package calendar;

import java.time.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class SimpleCalendar {

	// User could change these, for example to localize program to other regions
	// / time zones
	public static ZoneId z = ZoneId.systemDefault();
	public static Locale language = Locale.ENGLISH; // affects language of
													// months and weekdays

	public static JFrame frame = new JFrame();
	public static LocalDate today = LocalDate.now(z);
	public static SelectDays month = new SelectDays(today);
	public static Calendar calendar = new Calendar(new ArrayList<Event>());

	public static void main(String[] args) {
		Controller selectDay = new Controller(month);
		MonthViewPanel MVP = new MonthViewPanel(calendar);
		calendar.attach(selectDay);
		calendar.attach(MVP);
		month.attach(selectDay);
		month.attach(MVP);
		
		frame.setLayout(new FlowLayout());
		frame.setTitle("Calendar317");
		frame.setSize(900,400);
		frame.add(selectDay);

		// Finalize frame setup
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
