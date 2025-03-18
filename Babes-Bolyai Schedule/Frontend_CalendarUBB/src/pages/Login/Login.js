import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";
import { loginUser } from "../../services/api";
import { FaEnvelope, FaLock } from "react-icons/fa";
import logo from "../../assets/pictures/Universitatea_Babes_Bolyai_Logo.jpg";

function Login() {
    const [credentials, setCredentials] = useState({ email: "", password: "" });
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setCredentials((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const token = await loginUser({
                email: credentials.email,
                password: credentials.password,
            });
            localStorage.setItem("authToken", token);
            setError("");
            navigate("/add-event");
        } catch (error) {
            setError("Invalid email or password");
        }
    };

    return (
        <div className="login-background">
            {}
            <img src={logo} alt="UBB Logo" className="top-logo" />

            <div className="login-box">
                <h2 className="login-title">Conectează-te</h2>
                <form onSubmit={handleSubmit} className="login-form">
                    <div className="form-group">
                        <label className="form-label">Email</label>
                        <div className="input-icon-container">
                            <FaEnvelope className="input-icon" />
                            <input
                                type="email"
                                name="email"
                                placeholder="Email"
                                className="input-field"
                                style={{ paddingLeft: "34px" }}
                                value={credentials.email}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>
                    <div className="form-group">
                        <label className="form-label">Parolă</label>
                        <div className="input-icon-container">
                            <FaLock className="input-icon" />
                            <input
                                type="password"
                                name="password"
                                placeholder="Parolă"
                                className="input-field"
                                style={{ paddingLeft: "34px" }}
                                value={credentials.password}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>
                    <div className="form-footer">
                        <a href="/forgot-password" className="forgot-password-link">
                            Mi-am uitat parola
                        </a>
                    </div>
                    <button type="submit" className="login-button">
                        Intră în cont
                    </button>
                    {error && <p className="error-message">{error}</p>}
                </form>
            </div>
        </div>
    );
}

export default Login;
