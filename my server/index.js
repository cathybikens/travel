const express = require("express");
const mysql = require("mysql");
const connection = mysql.createConnection({
  host: "127.0.0.1",
  database: "matatusacco",
  user: "root",
  password: "",
});
const app = express();
// images, css, frontend js files are static files
//to render static files from express server we direct our server to the file from a sayed folder using a middleware
app.use(express.static("publicFiles"));

app.get("/", (req, res) => {
  connection.query("SELECT * FROM vehicles", (err, data) => {
    res.render("home.ejs", { vehicles: data });
  });
});
app.get("/contact", (req, res) => {
  res.render("contact.ejs");
});
app.listen(4000);
