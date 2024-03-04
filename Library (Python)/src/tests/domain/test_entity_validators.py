from unittest import TestCase

from src.ro.csubb.library.exceptions.exceptions import Validators, FormatIdError, SsnFormatError, StringFormatError

class TestValidators(TestCase):
    def setUp(self) -> None:
        pass

    def test_id_validator(self):
        with self.assertRaises(FormatIdError):
            Validators.validate_id('a')

    def test_ssn_validator(self):
        with self.assertRaises(SsnFormatError):
            Validators.validate_ssn('1a')

    def test_validate_string(self):
        with self.assertRaises(StringFormatError):
            Validators.validate_string('')

    def tearDown(self) -> None:
        pass
