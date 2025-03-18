import React, { useState } from "react";
import { FiUpload } from "react-icons/fi";
import "./CustomImageUpload.css";

function CustomImageUpload({ onImageChange }) {
    const [imageName, setImageName] = useState("");

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setImageName(file.name);
            onImageChange(file);
        }
    };

    const handleDelete = () => {
        setImageName("");
        onImageChange(null);
    };

    return (
        <div className="custom-file-upload">
            <div className="upload-block">
                <div className="textbox-container">
                    <input
                        type="text"
                        className="image-textbox"
                        placeholder="Încarcă o imagine"
                        value={imageName}
                        readOnly
                    />
                    {imageName && (
                        <button
                            className="clear-button"
                            onClick={handleDelete}
                            type="button"
                        >
                            &times;
                        </button>
                    )}
                </div>
                <label htmlFor="fileInput" className="upload-button">
                    <FiUpload size={16} />
                </label>
                <input
                    type="file"
                    id="fileInput"
                    className="file-input"
                    accept="image/*"
                    onChange={handleFileChange}
                />
            </div>
        </div>
    );
}

export default CustomImageUpload;
