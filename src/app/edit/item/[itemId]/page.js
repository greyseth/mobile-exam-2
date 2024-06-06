"use client";

import "../../../assets/css/form.css";

import { get, hostname, post, put } from "@/app/API";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function EditItem({ params }) {
  const [isLoading, setIsLoading] = useState(true);
  const [headerMessage, setHeaderMessage] = useState("Loading...");

  const [itemData, setItemData] = useState();

  async function fetchData() {
    const request = await get("/items/" + params.itemId);
    if (request.error || !request) {
      alert("Sebuah Kesalahan Terjadi!");
      console.log(request);
      return;
    }

    if (request.empty) {
      setHeaderMessage("Id Barang Tidak Ditemukan");
      return;
    }

    setItemData(request);
    setIsLoading(false);
  }

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <section className="main-content">
      {isLoading ? (
        <h1>{headerMessage}</h1>
      ) : (
        <EditForm itemData={itemData} setItemData={setItemData} />
      )}
    </section>
  );
}

function EditForm({ itemData, setItemData }) {
  const [updates, setUpdates] = useState({});
  const [tempImage, setTempImage] = useState();

  const router = useRouter();

  async function handleUpdate() {
    //Upload image to server
    let imageName = "";
    if (tempImage) {
      let formData = new FormData();
      formData.append("image_upload", tempImage);
      const request = await fetch(hostname + "/images", {
        method: "POST",
        body: formData,
      });
      const result = await request.json();

      if (result.success) {
        imageName = result.image;
      } else {
        alert("Sebuah Kesalahan Terjadi!");
        console.log(result);
        return;
      }
    }

    //Update item records
    let updateItemData = {};
    if (updates.name) updateItemData.name = itemData.name;
    if (updates.description) updateItemData.description = itemData.description;
    if (updates.price) updateItemData.price = itemData.price;
    if (updates.image) updateItemData.image = imageName;

    const updated = await post(
      "/items/update/" + itemData.item_id,
      updateItemData
    );
    if (!updated.error) {
      router.push("/items");
    } else {
      alert("Sebuah Kesalahan Terjadi!");
      console.log(updated);
      return;
    }
  }

  return (
    <>
      <h1 style={{ marginBottom: "2em" }}>Edit Barang {itemData.name}</h1>
      <div className="form">
        <input
          type="text"
          placeholder="Nama Barang"
          value={itemData.name}
          onChange={(e) => {
            setItemData({ ...itemData, name: e.target.value });
            setUpdates({ ...updates, name: true });
          }}
        />
        <textarea
          placeholder="Deskripsi Barang"
          value={itemData.description}
          onChange={(e) => {
            setItemData({ ...itemData, description: e.target.value });
            setUpdates({ ...updates, description: true });
          }}
        />
        <input
          type="number"
          placeholder="Harga Barang ($)"
          value={itemData.price}
          onChange={(e) => {
            setItemData({ ...itemData, price: e.target.value });
            setUpdates({ ...updates, price: true });
          }}
        />
        <p>Ubah Gambar Barang</p>
        <input
          type="file"
          accept="image/*"
          onChange={(e) => {
            setTempImage(e.target.files[0]);
            setUpdates({ ...updates, image: true });
          }}
        />

        <button onClick={handleUpdate}>UPDATE</button>
      </div>
    </>
  );
}
