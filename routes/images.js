const express = require("express");
const router = express.Router();
const bodyParser = require("body-parser");
const cors = require("cors");
const connection = require("../db");
const path = require("path");
const multer = require("multer");

const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, "./resources/uploads");
  },
  filename: function (req, file, cb) {
    let extArray = file.mimetype.split("/");
    let extension = extArray[extArray.length - 1];
    cb(null, "img_" + Date.now() + "." + extension);
  },
});
const upload = multer({ storage: storage });

router.post("/", upload.single("image_upload"), async (req, res) => {
  // const content = req.body;
  res.status(200).json({ success: true, image: req.file.filename });
});

router.get("/:filename", async (req, res) => {
  res.sendFile(
    path.resolve(__dirname + "/../resources/uploads/" + req.params.filename)
  );
});

module.exports = router;
