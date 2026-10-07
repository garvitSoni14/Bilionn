import Category from "../Category/Category";
import "./DisplayCategory.css";
import {assets} from '../../assets/assets.js';
const DisplayCategory = ({selectedCategory, setSelectedCategory, categories }) => {
    return (
        <div className="row g-3" style={{width:"100%", margine:0}}>
            <div key="all" className="col-md-3 col-sm-6" style={{padding: '0 10px'}}>
                <Category
                        categoryName = "All Items"
                        imgUrl={assets.items}
                        numberOfItems={categories.reduce((acc,cat)=> acc+cat.items, 0)}
                        bgColor="#6c757"
                        isSelected={selectedCategory === ""}
                        onClick={() => setSelectedCategory("")}
                />
            </div>
            {categories.map((category) => (
                <div key={category.categoryId} className="col-md-3 col-sm-6" style={{padding: '0 10px'}}>
                    <Category
                        categoryName = {category.name}
                        imgUrl={category.imgUrl ? `http://localhost:8080${category.imgUrl}` : assets.image_upload_icon}
                        numberOfItems={category.items}
                        bgColor={category.bgColor}
                        isSelected={selectedCategory === category.categoryId}
                        onClick={() => setSelectedCategory(category.categoryId)}
                    />
                </div>
            ))}
        </div>
    );
};
export default DisplayCategory;