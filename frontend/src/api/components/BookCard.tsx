import React, {FormEvent} from 'react';
import "./BookCard.css";
import {BookState} from "../model/BookState";
import axios from "axios";
import {BookModel} from "../model/BookModel";


type BookCardProps = {
    book: BookModel;
    reloadAllBooks: () => void;

}

function BookCard(props: BookCardProps) {
    const [newBook, setNewBook] = React.useState(
        {
            id: props.book.id,
            cover: props.book.cover,
            title: props.book.title,
            author: props.book.author,
            isbn: props.book.isbn,
            bookState: BookState.AVAILABLE
        }
    );


    const addNewBook = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        if (!newBook.title || !newBook.author || !newBook.isbn || !newBook.bookState) {
            alert(`Please fill book title, author, isbn and state`)
            return
        }

        axios.post("/api/books", newBook)
            .then(props.reloadAllBooks)
            .catch((e) => console.log("POST Error: " + e))
        setNewBook({
            id: "",
            cover: "",
            title: "",
            author: "",
            isbn: "",
            bookState: BookState.AVAILABLE
        });
    }

    function handleAddApiBook(event: any) {
        setNewBook({
            ...newBook,
            [event.target.name]: event.target.value
        })
    }

    return <>
        <div className={"book-card"}>
            <img src={props.book.cover} alt={props.book.cover}/>
            <h3 className="book-title">{props.book.title}</h3>
            <p className="book-authors">{props.book.author}</p>
            <p className="book-authors">{props.book.isbn}</p>
            <form onSubmit={addNewBook}>
                <div className="div-form">
                    <br/>
                    <label>
                        Cover:
                        <input className="input-text" type="text"
                               id={"cover" + props.book.cover}
                               name="cover"
                               value={props.book.cover}
                               placeholder="cover"
                               onChange={handleAddApiBook}
                        />
                    </label>
                    <br/>
                    <label>
                        Title:
                        <input className="input-text" type="text"
                               id={"title" + props.book.id}
                               name="title"
                               value={props.book.title}
                               placeholder="title"
                               onChange={handleAddApiBook}
                        />
                    </label>
                    <br/>
                    <label>
                        Author:
                        <input className="input-text" type="text"
                               id={"author" + props.book.id}
                               name="author"
                               value={props.book.author}
                               placeholder="author"
                               onChange={handleAddApiBook}
                        />
                    </label>
                    <br/>
                    <label>
                        ISBN:
                        <input className="input-text" type="text"
                               id={"isbn" + props.book.id}
                               name="isbn"
                               value={props.book.isbn}
                               placeholder="isbn"
                               onChange={handleAddApiBook}
                        />
                    </label>
                    <br/>
                    <label htmlFor="bookState">New Book State:</label>
                    <select name="bookState" id="bookState">
                        <option value={BookState.AVAILABLE}>AVAILABLE</option>
                        <option value={BookState.NOT_AVAILABLE}>NOT_AVAILABLE</option>
                    </select>
                    <br/><br/>
                </div>
                <button>ADD</button>
            </form>
        </div>


    </>
}

export default BookCard;