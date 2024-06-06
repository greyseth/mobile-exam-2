const express = require("express");
const router = express.Router();
const bodyParser = require("body-parser");
const cors = require("cors");
const connection = require("../db");

router.post("/login", async (req, res) => {
  const content = req.body;
  if (!content.email || !content.password)
    return res
      .status(400)
      .json({ error: "Missing one or more required parameters" });

  connection.query(
    `SELECT user_id, username FROM users WHERE email = '${content.email}' AND password = '${content.password}';`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else {
        if (rows.length <= 0) return res.status(200).json({ success: false });
        else
          return res.status(200).json({
            success: true,
            user_id: rows[0].user_id,
            username: rows[0].username,
          });
      }
    }
  );
});

router.post("/adminlogin", async (req, res) => {
  const content = req.body;
  if (!content.email || !content.password)
    return res
      .status(400)
      .json({ error: "Missing one or more required parameters" });

  connection.query(
    `SELECT user_id, username FROM users WHERE email = '${content.email}' AND password = '${content.password}' AND admin = 1;`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else {
        if (rows.length <= 0) return res.status(200).json({ success: false });
        else
          return res.status(200).json({
            success: true,
            user_id: rows[0].user_id,
            username: rows[0].username,
          });
      }
    }
  );
});

router.post("/signup", async (req, res) => {
  const content = req.body;
  if (!content.username || !content.email || !content.password)
    return res
      .status(400)
      .json({ error: "Missing one or more required parameters" });

  connection.query(
    `SELECT email FROM users WHERE email = '${content.email}';`,
    (err, rows, fields) => {
      //checks if account exists
      if (err) return res.status(500).json({ error: err });
      else {
        if (rows.length <= 0) {
          //continues to create account
          connection.query(
            `INSERT INTO users(username, email, password${
              content.admin ? ", admin" : ""
            }) 
                VALUES ('${content.username}', '${content.email}', '${
              content.password
            }'${content.admin ? ", TRUE" : ""})`,
            (err, rows, fields) => {
              if (err) return res.status(500).json({ error: err });
              return res.status(200).json({ user_id: rows.insertId });
            }
          );
        } else return res.status(500).json({ error: "existingemail" });
      }
    }
  );
});

router.get("/all", async (req, res) => {
  connection.query("SELECT * FROM users;", (err, rows, fields) => {
    if (err) res.status(500).json({ error: err });
    else return res.status(200).json(rows);
  });
});

router.get("/objectified/:user_id", async (req, res) => {
  connection.query(
    `SELECT * FROM users WHERE user_id = ${req.params.user_id};`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      return res.status(200).json({ data: rows[0] });
    }
  );
});

router.get("/:user_id", async (req, res) => {
  connection.query(
    `SELECT * FROM users WHERE user_id = ${req.params.user_id};`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      return res.status(200).json(rows[0]);
    }
  );
});

//Implement update if have time
router.put("/:user_id", async (req, res) => {});
router.delete("/:user_id", async (req, res) => {
  connection.query(
    `DELETE FROM users WHERE user_id = ${req.params.user_id}`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      return res.status(200).json({ success: true });
    }
  );
});

module.exports = router;
