/*
* JTerm - a cross-platform terminal
* Copyright (C) 2017 Sergix, NCSGeek
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package main.java.jterm;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements Runnable
{

	private static BufferedReader input;
	
	public void run()
	{
		
		while (true)
		{
			try
			{
				String output = Client.input.readLine();
				if (output != null)
					System.out.println(output);
				
			}
			catch (IOException ioe)
			{
				return;
				
			}
			
		}
		
	}
	
	public static void Connect(ArrayList<String> options)
	{
		
		String address = "0.0.0.0";
		String portInput = "80";
		boolean next = false;
		
		for (String option: options)
		{
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\tconnect [-h] [-p port] address\n\nConnect to the specified IP address using TCP/IP. Default address is \"0.0.0.0\". Default port is 80.");
				return;
				
			}
			else if (option.equals("-p"))
				next = true;

			else if (next)
			{
				portInput = option;
				next = false;
				
			}
			else
				address = option;
			
		}
		
		int i = 0;
        int port = 0;

        while( i < portInput.length())  
        {
            port *= 10;
            port += portInput.charAt(i++) - '0';
            
        }

		try
		{
			System.out.println("Connecting to " + address + ":" + port);
			
			Socket connection 					= new Socket(address, port);
			InputStream input 					= connection.getInputStream();
			OutputStream output 				= connection.getOutputStream();
			BufferedReader bufferedSocketInput 	= new BufferedReader(new InputStreamReader(input));
			
			Client.input 						= bufferedSocketInput;
			
			Client client 						= new Client();
			Thread readThread 					= new Thread(client);
			readThread.start();
			
			System.out.println("Connected to server. Enter a blank line to quit. Reading for input...");
			
			while (true)
			{
				BufferedReader bufferedSocketOutput = new BufferedReader(new InputStreamReader(System.in), 1);
				String line = bufferedSocketOutput.readLine();
				
				if (line.equals(""))
					break;
				
				output.write(line.getBytes());
				
				output.close();
				bufferedSocketOutput.close();
				
			}

			connection.close();
			
		}
		catch (IOException ioe)
		{
			System.out.println("Connection severed.");
			
		}
		
	}
	
}
