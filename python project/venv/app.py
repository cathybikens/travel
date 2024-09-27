from flask import Flask, request, jsonify
from payment import process_payment

app = Flask(__name__)

@app.route('/')
def home():
    return "Welcome to Beba Services"

@app.route('/pay', methods=['POST'])
def pay():
    data = request.get_json()
    phone_number = data.get('phone_number')
    amount = data.get('amount')
    if process_payment(phone_number, amount):
        return jsonify({"status": "success"}), 200
    else:
        return jsonify({"status": "failure"}), 500

if __name__ == '__main__':
    app.run(debug=True)
