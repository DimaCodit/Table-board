package com.dr.test;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EchoServer extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[65500];
    private Controller controller;

    public EchoServer(Controller controller) throws SocketException {
        socket = new DatagramSocket(controller.getPort());
        this.controller = controller;
    }

    public void run() {
        running = true;

        while (running) {
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String received = new String(packet.getData(), packet.getOffset(), packet.getLength(), Charset.forName("cp1251"));

            if (received.equals("end")) {
                running = false;
                continue;
            }

            //System.out.println(received);
            Reader reader = new StringReader(received);
            try {
                List list = readAll(reader);
                controller.clear();
                controller.fillByList(list);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        socket.close();
    }

    public List<String[]> readAll(Reader reader) throws Exception {
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();

        CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();
        List<String[]> list = new ArrayList<>();
        list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }
}