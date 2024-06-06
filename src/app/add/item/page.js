"use client";

import { hostname, post } from "@/app/API";
import "../../assets/css/form.css";

import PageForm from "@/app/components/PageForm";
import { useState } from "react";
import { Checkbox } from "@nextui-org/checkbox";

export default function AddItems() {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState("");
  const [image, setImage] = useState("");

  const [isLoading, setIsLoading] = useState(false);

  async function handleSubmit() {
    setIsLoading(true);

    //Uploads image first
    let imageName = undefined;
    if (image) {
      let formData = new FormData();
      formData.append("image_upload", image);

      const request = await fetch(hostname + "/images", {
        method: "POST",
        body: formData,
      });
      const result = await request.json();

      if (result.success) imageName = result.image;
      else {
        alert("Sebuah Kesalahan Terjadi!");
        console.log(result);
        return;
      }
    }

    //Then adds item data
    const addFetch = await post("/items", {
      name: name,
      description: description,
      price: price,
      image: imageName,
    });

    if (!addFetch.error && addFetch.item_id) {
      alert("Barang " + name + " telah ditambahkan");

      setName("");
      setDescription("");
      setPrice(undefined);
      setImage(undefined);
    } else {
      alert("Sebuah Kesalahan Terjadi!");
      console.log(addFetch);
    }

    setIsLoading(false);
  }

  return (
    <section className="main-content">
      <h1 style={{ marginBottom: "2em" }}>Tambahkan Barang</h1>
      {isLoading ? (
        <h1>Loading...</h1>
      ) : (
        <div className="form">
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Nama Barang"
          />
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Deskripsi Barang"
          />
          <input
            type="number"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
            placeholder="Harga Barang (Rp)"
          />
          <input
            type="file"
            accept="image/*"
            onChange={(e) => setImage(e.target.files[0])}
          />

          <button onClick={handleSubmit}>Tambahkan</button>
        </div>
      )}
    </section>
  );
}
