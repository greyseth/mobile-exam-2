const express = require("express");
const router = express.Router();
const bodyParser = require("body-parser");
const cors = require("cors");
const connection = require("../db");

//columns: name, description, price, image_id

//Deletes item (no shit)
router.delete("/:item_id", async (req, res) => {
  connection.query(
    `DELETE FROM items WHERE item_id = ${req.params.item_id}`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      return res.status(200).json({ success: true });
    }
  );
});

//Updates item
router.post("/update/:item_id", async (req, res) => {
  const content = req.body;

  let updatePrev = false;
  let query = "UPDATE items SET ";
  if (content.name) {
    query += `name = "${content.name}"`;
    updatePrev = true;
  }
  if (content.description) {
    query += `${updatePrev ? ", " : ""}description = "${content.description}"`;
    updatePrev = true;
  }
  if (content.price) {
    query += `${updatePrev ? ", " : ""}price = "${content.price}"`;
    updatePrev = true;
  }
  if (content.image) {
    query += `${updatePrev ? ", " : ""}image = "${content.image}"`;
  }

  query += " WHERE item_id = " + req.params.item_id + ";";

  connection.query(query, (err, rows, fields) => {
    if (err) return res.status(500).json({ error: err });
    return res.status(200).json({ success: true });
  });
});

//Uploads new item
router.post("/", async (req, res) => {
  const content = req.body;

  const name = content.name ?? "";
  const description = content.description ?? "";
  const price = content.price ?? 0;
  const image = content.image ?? null;

  connection.query(
    `INSERT INTO items(name, description, price${image ? ", image" : ""})
    VALUES ("${name}", "${description}", ${price}${
      image ? ", '" + image : ""
    }');`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      return res.status(200).json({ item_id: rows.insertId });
    }
  );
});

router.get("/search/:query", async (req, res) => {
  connection.query(
    `SELECT items.*, SUM(cart_item.quantity) AS orders_count FROM items LEFT JOIN cart_item ON items.item_id = cart_item.item_id WHERE LOWER(items.name) LIKE LOWER('%${req.params.query}%') GROUP BY items.item_id;`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else {
        if (rows.length > 0) {
          let newRows = [];
          for (let i = 0; i < rows.length; i++) {
            const el = rows[i];

            if (!el.orders_count) el.orders_count = 0;

            if (el.image) newRows.push(rows[i]);
            else newRows.push(omitProperty(rows[i], "image"));
          }

          return res.status(200).json({ data: newRows });
        } else return res.status(200).json({ empty: true });
      }
    }
  );
});

//Gets all items
router.get("/", async (req, res) => {
  connection.query(
    `SELECT items.*, SUM(cart_item.quantity) AS orders_count FROM items
  LEFT JOIN cart_item ON items.item_id = cart_item.item_id GROUP BY items.item_id;`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else {
        let newRows = [];
        for (let i = 0; i < rows.length; i++) {
          const el = rows[i];
          if (!el.orders_count) el.orders_count = 0;

          newRows.push(el);
        }

        return res.status(200).json(newRows);
      }
    }
  );
});
function omitProperty(obj, excludedProp) {
  const entries = Object.entries(obj).filter(([key]) => key !== excludedProp);
  return Object.fromEntries(entries);
}
//Because I have to return it as an object for mobile
router.get("/objectified", async (req, res) => {
  connection.query(
    `SELECT items.*, SUM(cart_item.quantity) AS orders_count FROM items
  LEFT JOIN cart_item ON items.item_id = cart_item.item_id GROUP BY items.item_id;`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else {
        if (rows.length > 0) {
          let newRows = [];
          for (let i = 0; i < rows.length; i++) {
            const el = rows[i];

            if (!el.orders_count) el.orders_count = 0;

            if (el.image) newRows.push(rows[i]);
            else newRows.push(omitProperty(rows[i], "image"));
          }

          return res.status(200).json({ data: newRows });
        } else return res.status(200).json({ empty: true });
      }
    }
  );
});

//Gets single item
router.get("/:item_id", async (req, res) => {
  connection.query(
    `SELECT items.*, SUM(cart_item.quantity) AS orders_count FROM items
    LEFT JOIN cart_item ON items.item_id = cart_item.item_id
    WHERE items.item_id = ${req.params.item_id} GROUP BY items.item_id;`,
    (err, rows, fields) => {
      if (err) return res.status(500).json({ error: err });
      else {
        if (rows.length > 0) {
          let newItem = rows[0];
          if (!newItem.orders_count) newItem.orders_count = 0;

          return res.status(200).json(newItem);
        } else return res.status(200).json({ empty: true });
      }
    }
  );
});

module.exports = router;
