from src.ro.csubb.library.repository.file_repository import FileRepository
from src.ro.csubb.library.service.book_service import BookService
from src.ro.csubb.library.service.client_service import ClientService
from src.ro.csubb.library.service.rent_service import RentService
from src.ro.csubb.library.ui.console import Console

if __name__ == '__main__':
    book_repository = FileRepository('book_repository')
    client_repository = FileRepository('client_repository')
    rent_repository = FileRepository('rent_repository')
    book_service = BookService(book_repository)
    client_service = ClientService(client_repository)
    rentBook_service = RentService(rent_repository, client_repository)
    console = Console(client_service, book_service, rentBook_service)
    console.menu()
