from bs4 import BeautifulSoup
import requests

address="http://www.ipaidabribe.com/reports/paid?page=#gsc.tab=0"
new_address="http://www.ipaidabribe.com/reports/paid?page=10#gsc.tab=0"
title_list = []
amount_list = []
department_list = []
details_list = []
city_list = []
date_list = []
state_list=[]
for i in range(0,1000,10):
    if(i==0):
        result = requests.get(address)
        soup = BeautifulSoup(result.text, "html.parser")
        bribe_list = soup.find("article").find_all("section", {"class": "ref-module-paid-bribe"})
        for bribe in bribe_list:
            title = bribe.find("h3").find("a")['title']
            print(title)
            title_list.append(title)
            department = bribe.find("li", {"class": "transaction"}).find("a")['title']
            print(department)
            department_list.append(department)
            price = bribe.find("li",{"class":"paid-amount"}).find("span").text.strip('Paid INR ').strip()
            print(price)
            amount_list.append(price)
            city = bribe.find("div", {"class": "key"}).find("a")['title'].split(",")[0]
            print(city)
            city_list.append(city)
            state = bribe.find("div", {"class": "key"}).find("a")['title'].split(",")[1]
            print(state)
            state_list.append(state)
            date = bribe.find("span", {"class": "date"}).text
            print(date)
            date_list.append(date)
            details = bribe.find("p", {"class": "body-copy-lg"}).find(text=True, recursive=False).strip()
            details_list.append(details)
            print(details)
            print('\n')

    else:
        new_address="http://www.ipaidabribe.com/reports/paid?page="+str(i)+"#gsc.tab=0"
        result=requests.get(new_address)
        soup = BeautifulSoup(result.text, "html.parser")
        bribe_list=soup.find("article").find_all("section",{"class":"ref-module-paid-bribe"})
        for bribe in bribe_list:
            title=bribe.find("h3").find("a")['title']
            print(title)
            title_list.append(title)
            department=bribe.find("li",{"class":"transaction"}).find("a")['title']
            print(department)
            department_list.append(department)
            price=bribe.find("li",{"class":"paid-amount"}).find("span").text.strip('Paid INR ').strip()
            print(price)
            amount_list.append(price)
            city=bribe.find("div",{"class":"key"}).find("a")['title'].split(",")[0]
            print(city)
            city_list.append(city)
            state=bribe.find("div",{"class":"key"}).find("a")['title'].split(",")[1]
            print(state)
            state_list.append(state)
            date = bribe.find("span", {"class": "date"}).text
            print(date)
            date_list.append(date)
            details = bribe.find("p", {"class": "body-copy-lg"}).find(text=True, recursive=False).strip()
            details_list.append(details)
            print(details)
            print('\n')

bribes = {
    'Title': title_list,
    'Department': department_list,
    'Details': details_list,
    'Amount': amount_list,
    'State': state_list,
    'City': city_list,
    'Date': date_list
}
import pandas as pd

data = pd.DataFrame(bribes)

data.to_csv('bribes_100.csv')




