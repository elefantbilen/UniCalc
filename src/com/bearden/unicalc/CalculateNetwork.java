package com.bearden.unicalc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.util.Log;

public class CalculateNetwork
{
	ArrayList<SubnetGroups> listOfsubnets;
	int ipAddrArray[];
	int networkByte = 3; //Yes, four bytes really but they are located in an array atm
	long totalPossibleAddresses = 0;
	int netWorkPrefix = 0;
	
	public CalculateNetwork(ArrayList<SubnetGroups> listOfSubnets, int ipAddrArray[], int netWorkPrefix)
	{
		this.listOfsubnets = listOfSubnets;
		this.ipAddrArray = ipAddrArray;
		this.netWorkPrefix = netWorkPrefix;
		sorter();
		print();
		findAddressSpace();
	}
	
	private void findAddressSpace()
	{
		while(networkByte >= 0)
		{
			if(ipAddrArray[networkByte] != 0)
				break;
			
			networkByte--;
		}
		
		Log.d("1", "pref: " + netWorkPrefix + " max: " + totalPossibleAddresses);
			totalPossibleAddresses = (long) Math.pow(2, (32 - netWorkPrefix));
		
		if(networkByte * 8 < netWorkPrefix)
		{
			//Do additional math here in case user dont want to subnet all possible nets in the prefix
		}
		
		totalPossibleAddresses -= 2;
		
		//if(sumOf(hostsNeeded) > totalPossibleAddresses) 
			//fail
		
		print();
	}
	
	private void getNumAddressesNeededForAllSubnets()
	{
		long total = 0;
		
		long biggest = listOfsubnets.get(0).getSubnetSizeNeeded();
		
		
	}
	
	private void sorter()
	{

		Collections.sort(listOfsubnets, new Comparator<SubnetGroups>()
		{
			@Override
			public int compare(SubnetGroups sub1, SubnetGroups sub2)
			{
				return sub2.getHostsWanted() -  sub1.getHostsWanted();
			}
		});

	}
	
	private void print()
	{
		
		//for(int i = 0; i < listOfsubnets.size(); i++)
			//Log.d("1", "" + listOfsubnets.get(i).getHostsWanted());
		
		//Log.d("1", "arr: " + ipAddrArray[2]);
			
		Log.d("1", "max: " + totalPossibleAddresses);
	}

}
