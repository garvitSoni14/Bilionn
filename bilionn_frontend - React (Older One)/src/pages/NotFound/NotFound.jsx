import { useNavigate } from "react-router-dom";
import "./NotFound.css";
const NotFound = () => {
    const navigate = useNavigate();
    return (
        <div className="notfound-wrapper">
            <div className="notfound-content">
                <h1 className="notfound-code">404</h1>
                <h2 className="notfound-message">Oops! Page Not Found</h2>
                <p className="notfound-description">
                    The page you're looking for doesn't exist or has been moved.
                </p>
                <button className="notfound-button" onClick={() => navigate("/")}>
                    Go to homepage
                </button>
            </div>
        </div>
    )
}
export default NotFound;