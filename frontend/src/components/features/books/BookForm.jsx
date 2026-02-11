import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { bookFormSchema } from '../../../utils/validators';
import { GENRES, LANGUAGES } from '../../../utils/constants';
import Input from '../../common/Input';
import Select from '../../common/Select';
import Textarea from '../../common/Textarea';
import Button from '../../common/Button';

/**
 * Book form component for creating and editing books
 */
const BookForm = ({ initialData = null, onSubmit, onCancel, isLoading = false }) => {
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm({
    resolver: zodResolver(bookFormSchema),
    defaultValues: initialData || {
      isAvailable: true,
      stockQuantity: 0,
      reviewCount: 0
    }
  });

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Title */}
        <div className="md:col-span-2">
          <Input
            label="Title"
            {...register('title')}
            error={errors.title?.message}
            required
            placeholder="Enter book title"
          />
        </div>

        {/* ISBN */}
        <Input
          label="ISBN"
          {...register('isbn')}
          error={errors.isbn?.message}
          required
          placeholder="978-0-1234-5678-9"
        />

        {/* Author */}
        <Input
          label="Author"
          {...register('author')}
          error={errors.author?.message}
          required
          placeholder="Enter author name"
        />

        {/* Genre */}
        <Select
          label="Genre"
          {...register('genre')}
          options={GENRES}
          error={errors.genre?.message}
          required
          placeholder="Select genre"
        />

        {/* Language */}
        <Select
          label="Language"
          {...register('language')}
          options={LANGUAGES}
          error={errors.language?.message}
          required
          placeholder="Select language"
        />

        {/* Publication Year */}
        <Input
          label="Publication Year"
          type="number"
          {...register('publicationYear')}
          error={errors.publicationYear?.message}
          required
          placeholder="2024"
        />

        {/* Publisher */}
        <Input
          label="Publisher"
          {...register('publisher')}
          error={errors.publisher?.message}
          placeholder="Publisher name"
        />

        {/* Pages */}
        <Input
          label="Pages"
          type="number"
          {...register('pages')}
          error={errors.pages?.message}
          placeholder="Number of pages"
        />

        {/* Price */}
        <Input
          label="Price"
          type="number"
          step="0.01"
          {...register('price')}
          error={errors.price?.message}
          placeholder="0.00"
        />

        {/* Stock Quantity */}
        <Input
          label="Stock Quantity"
          type="number"
          {...register('stockQuantity')}
          error={errors.stockQuantity?.message}
          placeholder="0"
        />

        {/* Rating */}
        <Input
          label="Rating"
          type="number"
          step="0.1"
          {...register('rating')}
          error={errors.rating?.message}
          placeholder="0.0 - 5.0"
        />

        {/* Review Count */}
        <Input
          label="Review Count"
          type="number"
          {...register('reviewCount')}
          error={errors.reviewCount?.message}
          placeholder="0"
        />

        {/* Cover Image URL */}
        <div className="md:col-span-2">
          <Input
            label="Cover Image URL"
            type="url"
            {...register('coverImageUrl')}
            error={errors.coverImageUrl?.message}
            placeholder="https://example.com/cover.jpg"
          />
        </div>

        {/* Description */}
        <div className="md:col-span-2">
          <Textarea
            label="Description"
            {...register('description')}
            error={errors.description?.message}
            rows={4}
            placeholder="Enter book description"
          />
        </div>

        {/* Is Available */}
        <div className="flex items-center gap-3">
          <input
            type="checkbox"
            id="isAvailable"
            {...register('isAvailable')}
            className="w-4 h-4 text-blue-600 rounded focus:ring-blue-500"
          />
          <label htmlFor="isAvailable" className="text-sm font-medium text-gray-700">
            Available for purchase
          </label>
        </div>
      </div>

      {/* Form actions */}
      <div className="flex justify-end gap-3 pt-6 border-t border-gray-200">
        <Button type="button" variant="secondary" onClick={onCancel}>
          Cancel
        </Button>
        <Button type="submit" disabled={isLoading}>
          {isLoading ? 'Saving...' : initialData ? 'Update Book' : 'Create Book'}
        </Button>
      </div>
    </form>
  );
};

export default BookForm;
