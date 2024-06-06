const express = require("express");
const router = express.Router();
const connection = require("../db");

router.post("/confirm/:order_id", async (req, res) => {
  const content = req.body;
  if (!content.status)
    return res.status(400).json({ error: "Missing status parameter" });

  connection.query(
    `UPDATE orders SET status = '${content.status}' WHERE order_id = ${req.params.order_id}`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else return res.status(200).json({ success: true });
    }
  );
});

router.post("/place", async (req, res) => {
  const content = req.body;
  if (!content.user_id)
    return res.status(400).json({ error: "Missing user_id parameter" });
  if (!content.total_price)
    return res.status(400).json({ error: "Missing total_price parameter" });

  connection.query(
    `INSERT INTO orders(user_id, total_price, status, date_ordered) VALUES(${content.user_id}, ${content.total_price}, 'pending', NOW());`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else {
        connection.query(
          `UPDATE cart_item SET order_id = ${rows.insertId} WHERE user_id = ${content.user_id} AND order_id IS NULL;`,
          (err2, rows2, fields2) => {
            if (err2) return res.status(500).json({ error: errr2 });
            else return res.status(200).json({ order_id: rows.insertId });
          }
        );
      }
    }
  );
});
//behind tomorrow nightmare (sucks for infiltration)(?) indomination captivation if darkness had a son here i am temptation is his father if darkness had a son here i am i bathe in holy water temptation leave me be yeah

router.get("/find/:order_id", async (req, res) => {
  connection.query(
    `SELECT * FROM orders WHERE order_id = ${req.params.order_id};`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else {
        if (rows.length > 0) return res.status(200).json(rows);
        else return res.status(200).json({ empty: true });
      }
    }
  );
});

router.post("/finddate", async (req, res) => {
  const content = req.body;
  if (!content.fromDate || !content.toDate)
    return res
      .status(400)
      .json({ error: "Missing one or more required parameters" });

  connection.query(
    `SELECT * FROM orders WHERE date_ordered > '${content.fromDate}' AND date_ordered < '${content.toDate}'`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else {
        if (rows.length > 0) return res.status(200).json(rows);
        else return res.status(200).json({ empty: true });
      }
    }
  );
});

router.get("/detail/:order_id", async (req, res) => {
  connection.query(
    `SELECT cart_item.*, items.name, items.price, items.image FROM cart_item LEFT JOIN items ON items.item_id = cart_item.item_id WHERE cart_item.order_id = ${req.params.order_id};`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      return res.status(200).json({ data: rows });
    }
  );
});

router.get("/from/:user_id", async (req, res) => {
  connection.query(
    `SELECT * FROM orders WHERE user_id = ${req.params.user_id}`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else {
        if (rows.length > 0) return res.status(200).json({ data: rows });
        else return res.status(200).json({ empty: true });
      }
    }
  );
});

router.get("/all", async (req, res) => {
  connection.query(`SELECT * FROM orders;`, (err, rows, fields) => {
    if (err) return res.status(500).json({ error: err });
    else return res.status(200).json(rows);
  });
});

router.get("/:order_id", async (req, res) => {
  connection.query(
    `SELECT * FROM orders WHERE order_id = ${req.params.order_id};`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else {
        if (rows.length > 0) return res.status(200).json({ data: rows[0] });
        else return res.status(200).json({ empty: true });
      }
    }
  );
});

module.exports = router;
