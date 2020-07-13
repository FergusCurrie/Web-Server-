package nz.ac.vuw.swen301.a2.client;

import com.google.gson.Gson;
import nz.ac.vuw.swen301.a2.client.LogEvent;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;



public class LogMonitor implements ActionListener{


    // Table Start
    String [][] marks = {};
    JComboBox jcb;
    JTextField textField;
    JFrame frame = new JFrame("LOG Monitor");


    public void makeWindow(){
        // Header
        JLabel header = new JLabel("min level : ");
        JLabel header2 = new JLabel("limit : ");


        // Combo box
        LogEvent.level_enum[] list = LogEvent.getEnums();
        JComboBox newC = new JComboBox(list);
        if(jcb != null){
            newC.setSelectedItem(jcb.getSelectedItem());
        }
        jcb = newC;


        // Text field
        JTextField newT = new JTextField();
        if(textField != null){
            newT.setText(textField.getText());
        }
        textField = newT;

        // Fetch data
        JButton fetchData = new JButton("FETCH DATA");
        fetchData.addActionListener(this);

        //Table
        String col[] = { "time", "level", "logger", "thread", "message"};
        Font font = new Font("Verdana", Font.PLAIN, 12);
        JTable table = new JTable(marks, col);
        table.setFont(font);
        table.setRowHeight(30);
        table.setBackground(Color.blue);
        table.setForeground(Color.white);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(Color.black);
        tableHeader.setForeground(Color.white);
        Font headerFont = new Font("Verdana", Font.PLAIN, 14);
        tableHeader.setFont(headerFont);

        // JFrame

        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(1,5));
        jp.add(header);
        jp.add(jcb);
        jp.add(header2);
        jp.add(textField);
        jp.add(fetchData);
        jp.setPreferredSize(new Dimension(400,40));

        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(jp,BorderLayout.PAGE_START);
        frame.getContentPane().add(new JScrollPane(table),BorderLayout.CENTER);

        frame.setVisible(true);
    }


    public static void main(String[] args){
        new LogMonitor().makeWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Get data
        String limit = textField.getText();
        String level = (String)jcb.getSelectedItem().toString();

        // Get query
        URI uri = null;
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost:8080").setPath("resthome4logs/logs")
                .addParameter("limit",limit).addParameter("level",level);
        try {
            uri = builder.build();
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }

        // create and execute the request
        HttpResponse response = null;
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);
        try {
            response = httpClient.execute(request);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String content = null;
        try {
            content = EntityUtils.toString(response.getEntity());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Convert to array
        Gson gson = new Gson();
        ArrayList<LogEvent> resArray = new ArrayList<LogEvent>();
        String add = "";
        for(int i = 1; i < content.length(); i++){
            if(content.charAt(i) == '}'){
                add += '}';
                LogEvent ee = gson.fromJson(add,LogEvent.class);
                resArray.add(ee);
                add = "";
                i++;
            }else {
                add += content.charAt(i);
            }

        }


        ArrayList<ArrayList<String>> arrayList = new ArrayList<ArrayList<String>>();
        for(LogEvent ee : resArray){
            ArrayList<String> line = new ArrayList<>();
            line.add(ee.getTimeStamp());
            line.add(ee.getLevel());
            line.add(ee.getLogger());
            line.add(ee.getThread());
            line.add(ee.getMessage());
            arrayList.add(line);
        }
        marks = new String[arrayList.size()][];
        for (int i = 0; i < arrayList.size(); i++) {
            ArrayList<String> row = arrayList.get(i);
            marks[i] = row.toArray(new String[row.size()]);
        }


        frame.getContentPane().removeAll();
        makeWindow();
    }
}
