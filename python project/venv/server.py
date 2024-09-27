from flask import Flask, request, jsonify
import africastalking
import requests
from requests.auth import HTTPBasicAuth
import datetime
import base64

app = Flask(__name__)

# Initialize Africa's Talking
username = "sandbox"  # Assuming you are using a sandbox environment
api_key = "atsk_2ca0c5880128a0d165cf6f11edf933f6d640928e2130f532b70581b8327955daef0b06d8"
africastalking.initialize(username, api_key)
ussd = africastalking.USSD

# M-Pesa credentials
consumer_key = 'dLJsS3o06ZUAtZu6hgmCXuRpAETBGJ6oHWkMkwP2akuOkNIH'
consumer_secret = 'IWnemzVlfDjMjt5WoR9nqU7E29Td5eSghgAeoAIbJwGvtGgzy8N89KPkoFBT2ZZE'
shortcode = 'your_shortcode'  # Replace with your shortcode
lipa_na_mpesa_online_shortcode = 'your_lnm_shortcode'  # Replace with your Lipa na M-Pesa Online shortcode
lipa_na_mpesa_online_passkey = 'your_lnm_passkey'  # Replace with your Lipa na M-Pesa Online passkey

# M-Pesa environment (True for sandbox, False for production)
sandbox = True

def get_access_token():
    url = 'https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials' if sandbox else 'https://api.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials'
    response = requests.get(url, auth=HTTPBasicAuth(consumer_key, consumer_secret))
    access_token = response.json()['access_token']
    return access_token

def lipa_na_mpesa_online(phone_number, amount, callback_url, account_reference, transaction_desc):
    access_token = get_access_token()
    api_url = 'https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest' if sandbox else 'https://api.safaricom.co.ke/mpesa/stkpush/v1/processrequest'
    headers = {
        'Authorization': 'Bearer {}'.format(access_token),
        'Content-Type': 'application/json'
    }
    timestamp = datetime.datetime.now().strftime('%Y%m%d%H%M%S')
    password = base64.b64encode(f"{lipa_na_mpesa_online_shortcode}{lipa_na_mpesa_online_passkey}{timestamp}".encode()).decode('utf-8')
    payload = {
        "BusinessShortCode": lipa_na_mpesa_online_shortcode,
        "Password": password,
        "Timestamp": timestamp,
        "TransactionType": "CustomerPayBillOnline",
        "Amount": amount,
        "PartyA": phone_number,
        "PartyB": lipa_na_mpesa_online_shortcode,
        "PhoneNumber": phone_number,
        "CallBackURL": callback_url,
        "AccountReference": account_reference,
        "TransactionDesc": transaction_desc
    }
    response = requests.post(api_url, json=payload, headers=headers)
    return response.json()

@app.route('/ussd', methods=['POST'])
def ussd_callback():
    session_id = request.values.get("sessionId", None)
    service_code = request.values.get("serviceCode", None)
    phone_number = request.values.get("phoneNumber", None)
    text = request.values.get("text", "default")

    if text == "" or text == "default":
        response = "CON Welcome to Beba Services\n"
        response += "1. Nairobi to Kitale - $10\n"
        response += "2. Nairobi to Eldoret - $8\n"
        response += "3. Eldoret to Nakuru - $5\n"
        response += "4. Nairobi to Mombasa - $12\n"
    elif text == "1":
        response = "CON Select Vehicle:\n1. Vehicle A\n2. Vehicle B\n"
    elif text == "2":
        response = "CON Select Vehicle:\n1. Vehicle C\n2. Vehicle D\n"
    elif text == "3":
        response = "CON Select Vehicle:\n1. Vehicle E\n2. Vehicle F\n"
    elif text == "4":
        response = "CON Select Vehicle:\n1. Vehicle G\n2. Vehicle H\n"
    else:
        response = "END Invalid option"

    return response

@app.route('/payment', methods=['POST'])
def mpesa_payment():
    phone_number = request.form['phone']
    amount = request.form['amount']
    callback_url = "https://7972-41-90-64-138.ngrok-free.app/mpesa_callback"  # Replace with your ngrok callback URL
    account_reference = "Beba Services"
    transaction_desc = "Payment for Beba Services"

    response = lipa_na_mpesa_online(phone_number, amount, callback_url, account_reference, transaction_desc)
    return jsonify(response)

@app.route('/mpesa_callback', methods=['POST'])
def mpesa_callback():
    data = request.get_json()
    # Process the payment response here
    print(data)
    return "Success"

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)








