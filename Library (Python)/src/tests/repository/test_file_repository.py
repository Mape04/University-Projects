from unittest import TestCase


class TestFileRepository(TestCase):
    def setUp(self) -> None:
        with open('repository/text_files/test_file_repo') as file:
            pass

    def test_add_entity(self):
        pass

    def test_update_entity(self):
        pass

    def test_delete_entity(self):
        pass

    def test_get_file_line_by_id(self):
        pass

    def test_file_delete_entity(self):
        pass

    def test_file_append_entity(self):
        pass

    def test_file_read(self):
        pass
