import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./AddEventPopup.css";
import CustomImageUpload from "./CustomImageUpload";

function AddEvent() {
    const [formData, setFormData] = useState({
        title: "",
        date: "",
        startTime: "",
        endTime: "",
        location: "",
        organizers: "",
        faculty: "",
        domain: "",
        description: "",
        link: "",
        image: null,
    });

    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("authToken");
        if (!token) {
            alert("Trebuie să te autentifici pentru a adăuga un eveniment!");
            navigate("/login");
        }
    }, [navigate]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleImageChange = (imageFile) => {
        setFormData((prev) => ({
            ...prev,
            image: imageFile,
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const requiredFields = [
            "title",
            "date",
            "startTime",
            "endTime",
            "location",
            "organizers",
            "faculty",
            "domain",
            "description",
        ];
        const emptyFields = requiredFields.filter((field) => !formData[field]);

        if (emptyFields.length > 0) {
            alert("Please fill in all required fields before submitting!");
            return;
        }

        console.log("Form submitted successfully", formData);
        alert("Eveniment adăugat cu succes!");
        navigate("/");
    };

    const facultyOptions = [
        "Facultatea de Matematică și Informatică",
        "Facultatea de Fizică",
        "Facultatea de Chimie și Inginerie Chimică",
        "Facultatea de Biologie și Geologie",
        "Facultatea de Geografie",
        "Facultatea de Știința și Ingineria Mediului",
        "Facultatea de Drept",
        "Facultatea de Litere",
        "Facultatea de Istorie și Filosofie",
        "Facultatea de Sociologie și Asistență Socială",
        "Facultatea de Psihologie și Științe ale Educației",
        "Facultatea de Științe Economice și Gestiunea Afacerilor",
        "Facultatea de Studii Europene",
        "Facultatea de Business",
        "Facultatea de Științe Politice, Administrative și ale Comunicării",
        "Facultatea de Educație Fizică și Sport",
        "Facultatea de Teologie Ortodoxă",
        "Facultatea de Teologie Greco-Catolică",
        "Facultatea de Teologie Reformată",
        "Facultatea de Teologie Romano-Catolică",
        "Facultatea de Teatru și Film",
        "Facultatea de Inginerie",
    ];

    const domainOptions = [
        "Conferință",
        "Workshop",
        "Seminar/Webinar",
        "Prezentare/Prelegere",
        "Dezbatere/Masă rotundă",
        "Teză de doctorat/Teză de abilitare",
        "Lansare de carte",
        "Seară de lectură",
        "Concert",
        "Spectacol de teatru",
        "Proiecție de film",
        "Expoziție",
        "Lansare/Încheiere proiect",
        "Curs adresat publicului larg",
        "Altele",
    ];

    return (
        <div className="add-event-page">
            <h1 className="popup-title">Adaugă un eveniment</h1>
            <form className="popup-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <div className="form-group">
                        <label><strong>Titlul evenimentului*</strong></label>
                        <input type="text" name="title" placeholder="Titlul evenimentului" value={formData.title} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label><strong>Data evenimentului*</strong></label>
                        <input type="date" name="date" value={formData.date} onChange={handleChange} required />
                    </div>
                </div>
                <div className="form-row">
                    <div className="form-group">
                        <label><strong>Ora de început*</strong></label>
                        <input type="time" name="startTime" value={formData.startTime} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label><strong>Ora de final*</strong></label>
                        <input type="time" name="endTime" value={formData.endTime} onChange={handleChange} required />
                    </div>
                </div>
                <div className="form-group">
                    <label><strong>Locația*</strong></label>
                    <input type="text" name="location" placeholder="Locația evenimentului" value={formData.location} onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label><strong>Organizator*</strong></label>
                    <input type="text" name="organizers" placeholder="Organizatorii evenimentului" value={formData.organizers} onChange={handleChange} required />
                </div>
                <div className="form-row">
                    <div className="form-group">
                        <label><strong>Facultatea*</strong></label>
                        <select name="faculty" value={formData.faculty} onChange={handleChange} required>
                            <option value="" disabled>Selectează facultatea</option>
                            {facultyOptions.map((faculty, index) => (
                                <option key={index} value={faculty}>{faculty}</option>
                            ))}
                        </select>
                    </div>
                    <div className="form-group">
                        <label><strong>Domeniul*</strong></label>
                        <select name="domain" value={formData.domain} onChange={handleChange} required>
                            <option value="" disabled>Selectează domeniul</option>
                            {domainOptions.map((domain, index) => (
                                <option key={index} value={domain}>{domain}</option>
                            ))}
                        </select>
                    </div>
                </div>
                <div className="form-group">
                    <label><strong>Descrierea evenimentului*</strong></label>
                    <textarea name="description" placeholder="Descrierea evenimentului" value={formData.description} onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label><strong>Link</strong></label>
                    <input type="url" name="link" placeholder="Link către eveniment" value={formData.link} onChange={handleChange} />
                </div>
                <div className="form-group">
                    <label><strong>Imagine</strong></label>
                    <CustomImageUpload onImageChange={handleImageChange} />
                </div>
                <div className="form-buttons">
                    <button type="submit" className="submit-button">Adaugă eveniment</button>
                </div>
            </form>
        </div>
    );
}

export default AddEvent;
