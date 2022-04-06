package calendar;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ListViewPanel extends JPanel {

	//instance variables
	private static ArrayList<Event> eventsOnAgenda = new ArrayList<>();
	String agenda ;
	JTextArea agendaDetail;

	/**
	 * Constructs an AgendaViewPanel object.
	 */
	public ListViewPanel() {


		// creating the AgendaViewPanel
		JPanel panel = new JPanel();
		agendaDetail = new JTextArea(); // where the agenda is displayed

		agenda = ""; // the string to save all the events
		agendaDetail.setText(agenda);
		panel.add(agendaDetail);

		// Make panel scrollable
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setPreferredSize(new Dimension(325, 200));
		add(scrollPane);
	}
	
	/**
	 * Updates the agendaView when a new event is added.
	 * @param month takes a month with a starting and ending dates.
	 */
	public void update(SelectDays month){
		agenda = "Events between " + month.startDate.toString() + " and " + month.endDate.toString() + '\n';
		Event.sort();
		eventsOnAgenda.clear() ;
		// save updated events to eventsOnAgenda
		for (Event event : Calendar.events) {
			if (event.getDate().equals(month.startDate) || event.getDate().equals(month.endDate))
				eventsOnAgenda.add(event);
			if (event.getDate().isAfter(month.startDate) && event.getDate().isBefore(month.endDate))
				eventsOnAgenda.add(event);
		}

		// display all the events on the agendaViewPanel
		for (Event e : eventsOnAgenda) {
			agenda += e.getDateStr() + " " + e.getStartTimeStr() + " " + e.getEndTimeStr() + " " + e.getTitle() + '\n';
		}
		
		if (eventsOnAgenda.size() == 0) agenda += "no events for selected period";

		agendaDetail.setText(agenda);
	}

}
