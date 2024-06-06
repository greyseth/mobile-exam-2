const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");
const multer = require("multer");

const app = express();
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(cors());

const userRouter = require("./routes/users");
const itemsRouter = require("./routes/items");
const imagesRouter = require("./routes/images");
const cartRouter = require("./routes/cart");
const ordersRouter = require("./routes/orders");

app.use("/users", userRouter);
app.use("/items", itemsRouter);
app.use("/images", imagesRouter);
app.use("/cart", cartRouter);
app.use("/orders", ordersRouter);

app.listen(3000, () => {
  console.log("RetroEmporium API is running");
});
