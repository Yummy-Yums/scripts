#!/usr/bin/python3
import os
import sys
from pathlib import Path, PurePath
import shutil

FILE_EXTENSIONS = {
    'Text Files': ['.txt', '.csv', '.json', '.xml', '.html'],
    'Programming Files': ['.py', '.java', '.cpp', '.js', '.php'],
    'Document Files': ['.docx', '.xlsx', '.pptx', '.pdf', '.odt'],
    'Image Files': ['.jpg', '.jpeg', '.png', '.gif', '.bmp', '.svg'],
    'Audio Files': ['.mp3', '.wav', '.aac', '.flac', '.m4a'],
    'Video Files': ['.mp4', '.avi', '.mkv', '.mov', '.wmv'],
    'Archive Files': ['.zip', '.rar', '.tar', '.gz', '.7z'],

}


# define method to create dummy files using the above

def populate_folder_with_dummy_files():
    path = Path("/home/eliasderby/Desktop/test")
    for file_ext in FILE_EXTENSIONS.values():
        for ext in file_ext:
            file_name = path / f"test1{ext}"
            file_name.touch()


def arranger(path):
    purified_path = PurePath(path)
    transformed_path = Path(purified_path)

    if not transformed_path.exists() or not transformed_path.is_dir():
        print(f"Path '{path}' does not exist. or is not a directory")
        return

    counter = 0
    # check if folders have been created , if not , create them
    for file in transformed_path.iterdir():

        if file.is_dir():
            counter += 1
            if file.name in FILE_EXTENSIONS:
                continue
            else:
                directory_name = transformed_path / file.name

                if not directory_name.exists():
                    try:
                        directory_name.mkdir()
                    except FileExistsError:
                        print(f"Folder '{directory_name}' already exists.")
                else:
                    print(f"Folder '{directory_name}' already exists.")

        elif file.is_file():
            counter += 1
            suffix = file.suffix

            # if path.is_file():
            for folder_name, corresponding_file_extensions in FILE_EXTENSIONS.items():
                if suffix in corresponding_file_extensions:
                    folder = transformed_path / folder_name
                    if not folder.exists():
                        try:
                            folder.mkdir()
                        except FileExistsError:
                            print(f"Folder '{folder}' already exists.")
                    try:
                        directory_name = transformed_path / file.name
                        if directory_name.exists():
                            shutil.copy2(str(file), str(folder))

                            file.unlink()
                        else:
                            shutil.move(str(file), str(folder))

                    except shutil.Error as error:
                        print(f"Error moving file '{file}' to '{folder}': {error}")

    if counter == 0: print("folder doesn't contain any files")


def main():

    # populate_folder_with_dummy_files()

    if len(sys.argv) < 2:
        # use "./"
        print("pass in a path")
        sys.exit(1)

    folder_path = sys.argv[1:]

    for elem in folder_path:
        arranger(elem)


if __name__ == '__main__':
    main()
