import requests
from bs4 import BeautifulSoup

from firebase import firebase

#give header

headers = {'User-Agent':'Mozilla/5.0'}

#create empty dictionary to save all news related data

all_news_data={}

#create session

with requests.Session() as session:
    session.headers = headers

    #we will scrap data from this link
    soup = BeautifulSoup(session.get("https://phys.org/earth-news/").text,"lxml")

    #create empty list to save all news links
    news_list=[]

    for news_div in soup.select(".news-link"):
        news_list.append(news_div.get("href"))

    #lets check for first 2 links

    i = 1

    for url in news_list[:10]:
        soup = BeautifulSoup(session.get(url).text,"lxml")
        mydivs = soup.findAll("div",{"class":"mt-4 article-main"})


        all_news_data[i] = [url,
                            soup.select_one(".article-img").select_one("img").get("src"),
                            soup.select_one(".article-img").select_one("img").get("alt"),
                            soup.select_one(".article-img").select_one("img").get("title").split("Credit")[0].strip(),
                            (mydivs[0].text).strip().split("\n")[3]]
        i+=1

config = {
    "apiKey":"AIzaSyA7MVcR2lo4aFCEmfE3BBueE6tiLhV24Xs",
    "authDomain":"inshorts-688b2.firebaseapp.com",
    "databaseURL":"https://inshorts-688b2-default-rtdb.firebaseio.com",
    "storageBucket":"inshorts-688b2.appspot.com"}

#create firebase database connection
firebaseconn = firebase.FirebaseApplication(config["databaseURL"],None)


for i in all_news_data:
    data = {"newsLink":all_news_data[i][0],
            "imagelink":all_news_data[i][1],
            "head":all_news_data[i][2],
            "title":all_news_data[i][3],
            "desc":all_news_data[i][4]}

    result = firebaseconn.patch("/News/%s"%i,data)
    print(result)
