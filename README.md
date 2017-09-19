# CSE5914
Capstone course for AI


# NLC Info

Currently training NLC to classify lights on/lights off 

Classifier ID(NEEDS TO BE UPDATED WITH EVERY NEW TRAINING SET): 6a2a04x217-nlc-28653
URL: https://gateway.watsonplatform.net/natural-language-classifier/api
Username: a475cc56-93c6-4b1c-9cc5-f76d8af50830
Password: rFlhaBf2aEtS

CHECK STATUS:
curl -u "a475cc56-93c6-4b1c-9cc5-f76d8af50830":"rFlhaBf2aEtS"  "https://gateway.watsonplatform.net/natural-language-classifier/api/v1/classifiers/6a2a04x217-nlc-28653"

ASK TO CLASSIFY:
curl -G -u "a475cc56-93c6-4b1c-9cc5-f76d8af50830":"rFlhaBf2aEtS" "https://gateway.watsonplatform.net/natural-language-classifier/api/v1/classifiers/6a2a04x217-nlc-28653/classify?text=lights%2F"
