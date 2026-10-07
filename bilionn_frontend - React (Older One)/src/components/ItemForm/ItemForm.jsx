import { useState } from "react";
import toast from "react-hot-toast";
import { assets } from "../../assets/assets";
import { useContext } from "react";
import { AppContext } from "../../context/AppContext";
import { addItem } from "../../Service/ItemService";

const ItemForm = () => {
    const {categories, setItemsData, itemsData, setCategories} = useContext(AppContext);
    const [image, setImage] = useState(false);
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState({
        name:"",
        categoryId:"",
        price:0,
        description:""
    });

    const onChangeHandler = (e) => {
        const value = e.target.value;
        const name = e.target.name;
        setData((data)=>({
            ...data,
            [name]: value
        }));
    }
    
    const onSubmitHandler = async (e) => {
        e.preventDefault();
        setLoading(true);
        if(!image){
                toast.error("Please select an image");
                return ;
            }

        const formData = new FormData();
        formData.append("image", image);
        formData.append("name", data.name);
        formData.append("categoryId", data.categoryId);
        formData.append("price", data.price);
        formData.append("description", data.description);
        try{
            
            const response = await addItem(formData);
            if(response.status === 201){
                setItemsData([...itemsData, response.data]);
                setCategories((prevCategories) => 
                    prevCategories.map((category) => category.categoryId === data.categoryId ? {...category, items: category.items + 1} : category));
                toast.success("Item added");
                setData({
                    name:"",
                    categoryId:"",
                    price:0,
                    description:""
                });
                setImage(false);
            }else{
                toast.error("Error in adding item");
            }
        }
        catch(error){
            console.error(error);
            toast.error("Error in adding item");
        }
        finally{
            setLoading(false);
        }
    }
    return (
        <div className="item-form-container" style={{height:'100vh', overflowY:'auto', overflowX:'hidden'}}>
            <div className="mx-2 mt-2">
            <div className="row">
                <div className="card col-md-8 form-container">
                    <div className="card-body">
                        <form onSubmit={onSubmitHandler}>
                            <div className="mb-3">
                                <label htmlFor="image" className="form-label">
                                    <img src={image ? URL.createObjectURL(image) : assets.image_upload_icon} alt="" width={48}/>
                                </label>
                                <input type="file" name="image" id="image" className="form-control" hidden onChange={(e) => setImage(e.target.files[0])} />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="name" className="form-label">Name</label>
                                <input type="text" 
                                    name="name" 
                                    id="name" 
                                    className="form-control" 
                                    placeholder="Item Name" 
                                    onChange={onChangeHandler}
                                    value={data.name}
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label className="form-label" htmlFor="category">
                                    Category
                                </label>
                                <select name="categoryId" id="category" className="form-control" 
                                    onChange={onChangeHandler}
                                    value={data.categoryId}
                                    required
                                    >
                                    <option value="">--SELECT CATEGORY--</option>
                                    {
                                        categories.map((category, index) => (
                                            <option key={index} value={category.categoryId}>{category.name}</option>
                                        ))
                                    }
                                </select>
                            </div>
                            <div className="mb-3">
                                <label htmlFor="price" className="form-label">Price</label>
                                <input type="number" name="price" id="price" className="form-control" placeholder="&#8377;200.00" 
                                onChange={onChangeHandler}
                                value={data.price}
                                required
                                />
                            </div>
                             <div className="mb-3">
                                <label htmlFor="description" className="form-label">Description</label>
                                <textarea
                                rows="5"
                                name="description"
                                id="description"
                                className="form-control"
                                placeholder="Write content here.."
                                onChange={onChangeHandler}
                                value={data.description}>
                                </textarea>
                            </div>
                           
                            <button type="submit" className="btn btn-primary w-100" disabled={loading}>{loading ? "Loading..." : "Save"}</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        </div>
    )
}

export default ItemForm;