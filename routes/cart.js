const express = require("express");
const router = express.Router();
const bodyParser = require("body-parser");
const cors = require("cors");
const connection = require("../db");

router.put("/:cart_item_id", async (req, res) => {
  const content = req.body;
  if (!content.increment)
    return res
      .status(400)
      .json({ error: 'Missing required "increment" paraameter' });

  connection.query(
    `UPDATE cart_item SET quantity = quantity ${
      content.increment ? "+" : "-"
    } 1 WHERE cart_item_id = ${req.params.cart_item_id}`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else return res.status(200).json({ success: true });
    }
  );
});

//What in the fuck is this?
// router.post("/order/:user_id", async (req, res) => {
//   const content = req.body;
//   if (!content.cart)
//     return res.status(400).json({ error: "Missing cart parameter" });
//   if (!content.total_price)
//     return res.status(400).json({ error: "Missing total_price parameter" });

//   connection.query(
//     `INSERT INTO orders(user_id, total_price, status, date_ordered) VALUES(${req.params.user_id}, ${content.total_price}, 'pending', CURDATE());`,
//     (err, rows, fields) => {
//       if (err) return res.status(500).json({ error: err });

//       const orderId = rows.insertId;
//       connection.query(
//         `UPDATE cart_item SET order_id = ${orderId} WHERE user_id = ${req.params.user_id} AND order_id IS NULL`,
//         (err, rows, fields) => {
//           if (err) return res.status(500).json({ error: err });
//           res.status(200).json({ order_id: orderId });
//         }
//       );
//     }
//   );
// });

router.post("/order", async (req, res) => {
  const content = req.body;
  if (!content.item_id)
    return res.status(400).json({ error: "Missing item_id parameter" });
  if (!content.user_id)
    return res.status(400).json({ error: "Missing user_id parameter" });

  connection.query(
    `INSERT INTO cart_item(item_id, user_id, quantity) VALUES (${content.item_id}, ${content.user_id}, 1);`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else return res.status(200).json({ success: true });
    }
  );
});

router.post("/updateorder", async (req, res) => {
  const content = req.body;
  if (!content.item_id)
    return res.status(400).json({ error: "Missing item_id parameter" });
  if (!content.user_id)
    return res.status(400).json({ error: "Missing user_id parameter" });
  if (!content.quantity)
    return res.status(400).json({ error: "Missing quantity parameter" });

  connection.query(
    `UPDATE cart_item SET quantity = ${content.quantity} WHERE item_id = ${content.item_id} AND user_id = ${content.user_id};`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else return res.status(200).json({ success: true });
    }
  );
});

router.post("/removeorder", async (req, res) => {
  const content = req.body;
  if (!content.user_id)
    return res.status(500).json({ error: "Missing user_id parameter" });
  if (!content.item_id)
    return res.status(500).json({ error: "Missing item_id parameter" });

  connection.query(
    `DELETE FROM cart_item WHERE user_id = ${content.user_id} AND item_id = ${content.item_id}`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else return res.status(200).json({ success: true });
    }
  );
});

router.get("/objectified/:user_id", async (req, res) => {
  //do later
  const query = `SELECT items.item_id, items.name, items.price, items.image, cart_item.quantity, users.user_id FROM items
    LEFT JOIN cart_item ON items.item_id = cart_item.item_id 
    LEFT JOIN users ON cart_item.user_id = users.user_id
    WHERE users.user_id = ${req.params.user_id} AND cart_item.order_id IS NULL GROUP BY items.item_id;`;

  connection.query(query, (err, rows, fields) => {
    if (err) return res.status(500).json({ error: err });
    return res.status(200).json({ data: rows });
  });
});

router.get("/:user_id", async (req, res) => {
  //do later
  const query = `SELECT items.item_id, items.name, items.price, items.image, cart_item.quantity, users.user_id FROM items
    LEFT JOIN cart_item ON items.item_id = cart_item.item_id 
    LEFT JOIN users ON cart_item.user_id = users.user_id
    WHERE users.user_id = ${req.params.user_id} AND cart_item.order_id IS NULL GROUP BY items.item_id;`;

  connection.query(query, (err, rows, fields) => {
    if (err) return res.status(500).json({ error: err });
    else {
      if (rows.length > 0) return res.status(200).json({ data: rows });
      else return res.status(200).json({ empty: true });
    }
  });
});

router.post("/", async (req, res) => {
  const content = req.body;
  if (!content.user_id || !content.item_id)
    return res
      .status(400)
      .json({ error: "Missing one or more required parameters" });

  connection.query(
    `INSERT INTO cart_item(user_id, item_id, quantity) VALUES (${
      content.user_id
    }, ${content.item_id}, ${content.quantity ?? 1});`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      return res.status(200).json({ cart_item_id: rows.insertId });
    }
  );
});

router.delete("/:cart_item_id", async (req, res) => {
  connection.query(
    `DELETE FROM cart_item WHERE cart_item_id = ${req.params.cart_item_id}`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      res.status(200).json({ success: true });
    }
  );
});

module.exports = router;
