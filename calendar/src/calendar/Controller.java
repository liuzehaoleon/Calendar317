package calendar;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class is the main view and controller of the project. All panels are
 * created or composed in this class.
 */

public class Controller extends JPanel implements ChangeListener {

	// instance variables
	private JLabel monthYear = new JLabel();
	public SelectDays month;
	private ArrayList<JTextField> labels = new ArrayList<>();
	private ArrayList<LocalDate> dates = new ArrayList<>();
	private Event generalEvent = new Event();
	
	private DayViewPanel dayViewPanel = new DayViewPanel();
	private WeekViewPanel weekViewPanel = new WeekViewPanel();
	private MonthViewPanel monthViewPanel = new MonthViewPanel(SimpleCalendar.calendar);
	private ListViewPanel listViewPanel = new ListViewPanel();
	ArrayList<Event> recurringEvents;
	int currentView = 1;
	File currentDirectory = new File(".");

	/**
	 * Constructs a ControllView object.
	 * 
	 * @param month takes a month with a date
	 */
	public Controller(SelectDays monthViewForSelect) {

		// initialize instance variables
		this.month = monthViewForSelect;
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		Calendar.events = generalEvent.loadEvents();
		
		JPanel monthView = new JPanel(); // The complete Month Panel
		JPanel monthViewDays = new JPanel();
		JPanel navLine = new JPanel();
		JPanel rightView = new JPanel();
		JPanel navPanel = new JPanel();
		
		//SelectDays seperateMonth = new SelectDays(month.date);
		JMenuBar bar=new JMenuBar();
		JMenu selectView = new JMenu("ViewChange");
		JMenuItem cDay = new JMenuItem("ChangeByDays");
		JMenuItem cWeek = new JMenuItem("ChangeByWeeks");
		JMenuItem cMonth = new JMenuItem("ChangeByMonths");
		
		JTextField previous = new JTextField("<");
		JTextField next = new JTextField(">");
		
//		JButton cList = new JButton("List View");
		JButton create = new JButton("CreateEvent");
		JButton delete = new JButton("DeleteEvent");
		JButton today = new JButton("Today");
		JButton listPanelBtn = new JButton("List");
		JButton quit = new JButton("Quit");
		
		//monthView
		monthView.setLayout(new BoxLayout(monthView, BoxLayout.Y_AXIS));
		monthView.setSize(800, 800);
		monthViewDays.setLayout(new GridLayout(7, 7));
		monthYear.setText(month.monthYearString()); // "June 2017"
		
		//previous and next fucntion
		previous.setEditable(false);
		next.setEditable(false);
		previous.setMaximumSize(new Dimension(22, 16));
		next.setMaximumSize(new Dimension(22, 16));
		
		//select view function
		selectView.setMnemonic(KeyEvent.VK_F);
		selectView.add(cDay);selectView.add(cWeek);selectView.add(cMonth);
		bar.add(selectView);
		
		// Right view with NavPanel and DayViewPanel
		rightView.setLayout(new BoxLayout(rightView, BoxLayout.Y_AXIS));
		navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
		navPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
		
		// navLine setting and add component
		navLine.setLayout(new BoxLayout(navLine, BoxLayout.X_AXIS));
		navLine.add(monthYear);
		navLine.add(previous);
		navLine.add(next);
		navLine.add(bar);
		monthView.add(navLine);
		
		//listener
		previous.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (currentView == 1)
					month.changeDate(month.date.minusDays(1));
				if (currentView == 2)
					month.changeDate(month.date.minusWeeks(1));
				if (currentView == 3) {
					month.changeDate(month.date.minusMonths(1));
//					seperateMonth.changeDate(seperateMonth.date.minusMonths(1));
//					updateMonth(seperateMonth);
				}
			}
		});	
		
		next.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (currentView == 1)
					month.changeDate(month.date.plusDays(1));
				if (currentView == 2)
					month.changeDate(month.date.plusWeeks(1));
//					seperateMonth.changeDate(seperateMonth.date.plusMonths(1));
//					updateMonth(seperateMonth);
				if (currentView == 3) {
					month.changeDate(month.date.plusMonths(1));				
//					seperateMonth.changeDate(seperateMonth.date.plusMonths(1));
//					updateMonth(seperateMonth);
				}
			}
		});

		
		cDay.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				currentView=1;
				rightView.removeAll();
				rightView.add(dayViewPanel);
				rightView.revalidate();
				rightView.repaint();
				selectView.setText("DailyView");
			}
		});
		
		cWeek.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				currentView=2;
				rightView.removeAll();
				rightView.add(weekViewPanel);
				rightView.revalidate();
				rightView.repaint();
				selectView.setText("WeeklyView");
			}
		});
		
		cMonth.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				currentView=3;
				rightView.removeAll();
				rightView.add(monthViewPanel);
				monthViewPanel.stateChanged(new ChangeEvent(this));
				rightView.revalidate();
				rightView.repaint();
				selectView.setText("MonthlyView");
			}
		});
		
//		cList.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e) {
//				currentView=4;
//				rightView.removeAll();
//				rightView.add(listViewPanel);
//				rightView.revalidate();
//				rightView.repaint();
//			}
//		});

		// display calendar on the left
		String[] weekdays = month.weekdays(); // Localized weekdays in short format

		for (int day = 1; day < 8; day++) {
			JLabel weekday = new JLabel(weekdays[day]);
			weekday.setHorizontalAlignment(SwingConstants.CENTER);
			monthViewDays.add(weekday);
		}

		LocalDate[] currentMonth = month.getDateArray(); // Get all 42 days that are displayed in the view
		for (LocalDate date : currentMonth) {// *********************************************************************
			JTextField label = new JTextField(String.valueOf(date.getDayOfMonth()));
			label.setVisible(false);

			if (date.getMonth() == Calendar.today.getMonth() && date.getYear() == Calendar.today.getYear()) {
				label.setVisible(true);
			}

			label.setEditable(false);
			label.setHorizontalAlignment(JTextField.CENTER);

			// If date is today or the selected color it red
			if (date.equals(Calendar.today) || date.equals(month.date)) {
				label.setBackground(Color.red);
			} else {
				label.setBackground(Color.white);
			}
			labels.add(label);
			dates.add(date);

			// Add MouseListener
			label.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					month.changeDate(getDateOfLbl(labels.indexOf(label)));
				}
			});
			monthViewDays.add(label);
		}
		monthView.add(monthViewDays);
		
		// Action for create button, opens new frame: CreateView
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CreateEventFrame(month);
			}
		});

		// Action for today button, sets date of month to today
		today.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				month.changeDate(Calendar.today);
			}
		});

		// Quit button, saves events and closes frame
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generalEvent.saveEvents(Calendar.events);
				System.exit(0);
			}
		});
		
		// Action for Agenda button, changes view to Agenda view
		listPanelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// pop out a new window
				final JFrame frame = new JFrame();
				JPanel panel = new JPanel();
				panel.setLayout(new FlowLayout());
				// add buttons and textFiled to the pop out window
				JLabel start = new JLabel("Start Date:");
				final JTextField startTxt = new JTextField("01/01/2022");
				JLabel end = new JLabel("End Date:");
				final JTextField endTxt = new JTextField("08/01/2022");
				JButton cancel = new JButton("Cancel");
				JButton ok = new JButton("OK");
				// add actionListener to cancel button
				cancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						frame.dispose();

					}
				});

				// add actionListener to OK button
				ok.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try { // update the month.startDate and month.endDate
							SimpleCalendar.month.startDate = LocalDate.parse(startTxt.getText(), Calendar.formatter);
							SimpleCalendar.month.endDate = LocalDate.parse(endTxt.getText(), Calendar.formatter);
							// create new ListView

							listViewPanel.update(month);
							rightView.removeAll();
							rightView.add(listViewPanel);
							rightView.revalidate();
							rightView.repaint();
							frame.dispose();
						} catch (Exception e2) {
							System.out.println("error");
						}
						;
					}
				});

				panel.add(start);
				panel.add(startTxt);
				panel.add(end);
				panel.add(endTxt);
				panel.add(ok);
				panel.add(cancel);

				frame.add(panel);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
			}
		});
		
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// pop out a new window
				final JFrame frame = new JFrame();
				JPanel panel = new JPanel();
				panel.setLayout(new FlowLayout());


				JLabel txt = new JLabel("Enter the title of the event you want to delete: ");
				final JTextField inputTitle = new JTextField("(Example, delete this line and add the name)");
				JButton delete = new JButton("Delete");
				JButton cancel = new JButton("Cancel");

				delete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						boolean result = SimpleCalendar.calendar.remove(inputTitle.getText());
						
						if (result == true){
							System.out.println("Event removed");
						}
						else{
							System.out.println("Event not found");
						}

					}
				});

				// add actionListener to cancel button
				cancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						frame.dispose();
					}
				});

				panel.add(txt);
				panel.add(inputTitle);
				panel.add(delete);
				panel.add(cancel);

				frame.add(panel);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
			}

		});

		// Add all buttons to the nav Panel
		navPanel.add(today);
		navPanel.add(create);
		navPanel.add(delete);
		navPanel.add(listPanelBtn);
		navPanel.add(quit);

		// right View show week view first
		rightView.add(dayViewPanel);

		// Add navPanel, monthView, rightView in left to right order.
		add(navPanel);
		add(monthView);
		add(rightView);
	}

	/**
	 * Given an index of a JLabel in the labels array, this method returns the
	 * corresponding LocalDate object.
	 * 
	 * @param index takes index of label
	 * @return the date of a label in the array as LocalDate
	 */
	public LocalDate getDateOfLbl(int index) {
		return dates.get(index);
	}

	/**
	 * Updates the calendar month to the given month.
	 * 
	 * @param m the new month
	 */
	public void updateMonth(SelectDays m) {
		int j = 0;
		monthYear.setText(m.monthYearString());
		for (LocalDate newDate : m.getDateArray()) {
			JTextField label = labels.get(j);
			label.setText(String.valueOf(newDate.getDayOfMonth()));
			label.setBackground(Color.white);
			j++;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		dayViewPanel.update(month);
		weekViewPanel.update(month);
		monthViewPanel.stateChanged(new ChangeEvent(this));
		listViewPanel.update(month);

		int j = 0;
		monthYear.setText(this.month.monthYearString());
		dates.clear();
		for (LocalDate newDate : this.month.getDateArray()) {
			JTextField label = labels.get(j);

			dates.add(newDate);
			label.setText(String.valueOf(newDate.getDayOfMonth()));
			if (Calendar.today.equals(newDate)) {
				label.setBackground(Color.red);
			} else if (this.month.date.equals(newDate)) {
				label.setBackground(Color.gray);
			} else {
				label.setBackground(Color.white);
			}
			j++;
		}
	}

}
